package ui;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Controller {

  private final int ZOOM_AMOUNT = 20;

  private final FileChooser importFileChooser = new FileChooser();

  private final FilterBarManager filterBarManager = new FilterBarManager();

  private final ObjectProperty<Image> currentImage = new SimpleObjectProperty<>();

  private final FilterParameterManager filterParameterManager = new FilterParameterManager();

  private final ToggleGroup displayMode = new ToggleGroup();

  private final DoubleProperty scaleFactor = new SimpleDoubleProperty(1);

  private Image filteredImage;

  private final ObjectProperty<Image> originalImage = new SimpleObjectProperty<>();

  @FXML private VBox rootVBox;

  @FXML private ImageView imageView;

  @FXML private ComboBox<String> zoomComboBox;

  @FXML private StackPane dropAreaInformation;

  @FXML private ToggleButton toggleOriginal;

  @FXML private ToggleButton toggleSplitScreen;

  @FXML private BorderPane mainPane;

  @FXML private HBox filterBar;

  @FXML private Label selectedFilterName;

  @FXML private VBox filterParametersContainer;

  @FXML
  private void initialize() {
    initZoomComboBox();
    initFileChoosers();
    initDisplayModeToggle();
    initFilterBar();
    initParameterControl();
    initAccelerators();
    imageView.imageProperty().bind(currentImage);
    imageView.scaleXProperty().bind(scaleFactor);
    imageView.scaleYProperty().bind(scaleFactor);
    dropAreaInformation.visibleProperty().bind(currentImage.isNull());

    filterBarManager.selectedFilter.addListener(
        (obs, n, o) -> {
          if (o != null) applyNewFilter();
        });
    filterParameterManager.changed.addListener((o, b, a) -> applyNewFilter());

    if (InputImageArg.INSTANCE.getFile() != null) {
      setNewImage(InputImageArg.INSTANCE.getFile());
    }
  }

  @FXML
  private void onImportClick() {
    try {
      File file = importFileChooser.showOpenDialog(rootVBox.getScene().getWindow());
      setNewImage(file);
    } catch (NullPointerException exception) {
      System.out.println("Dialog aborted!");
    }
  }

  @FXML
  private void onExportClick() {
    if (currentImage.getValue() != null) {
      BufferedImage bi = SwingFXUtils.fromFXImage(currentImage.getValue(), null);
      FileChooser fc = new FileChooser();
      File outFile = fc.showSaveDialog(rootVBox.getScene().getWindow());

      try{
        ImageIO.write(bi, "png", outFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void onZoomInClick() {
    int newZoom = Integer.parseInt(zoomComboBox.getValue().replace("%", "")) + ZOOM_AMOUNT;
    zoomComboBox.valueProperty().setValue(newZoom + "%");
  }

  @FXML
  private void onZoomOutClick() {
    int newZoom = Integer.parseInt(zoomComboBox.getValue().replace("%", "")) - ZOOM_AMOUNT;
    zoomComboBox.valueProperty().setValue(newZoom + "%");
  }

  @FXML
  private void onFileDrop(DragEvent event) {
    Dragboard dragboard = event.getDragboard();
    if (dragboard.hasFiles()) {
      setNewImage(dragboard.getFiles().get(0));
    }
    dragboard.clear();
  }

  private void initZoomComboBox() {
    zoomComboBox
        .valueProperty()
        .addListener(
            (obs, oldItem, newItem) -> {
              if (newItem.matches("\\d+%?")) {
                scaleFactor.setValue(Integer.parseInt(newItem.replace("%", "")) / 100.0);
              } else {
                zoomComboBox.setValue(oldItem);
              }
            });
  }

  private void initFileChoosers() {
    File home = new File(System.getProperty("user.home"));
    FileChooser.ExtensionFilter imageFilter =
        new FileChooser.ExtensionFilter("image", "*jpg", "*.jpeg", "*.png");

    importFileChooser.setInitialDirectory(home);
    importFileChooser.setTitle("Select Image");
    importFileChooser.getExtensionFilters().add(imageFilter);
  }

  private void initDisplayModeToggle() {
    toggleOriginal.setToggleGroup(displayMode);
    toggleSplitScreen.setToggleGroup(displayMode);
    toggleOriginal.setUserData(0);
    toggleSplitScreen.setUserData(1);
    displayMode
        .selectedToggleProperty()
        .addListener(
            (obs, oldT, newT) -> {
              if (originalImage.getValue() == null) return;
              if (oldT != null && (int) oldT.getUserData() == 1) {
                mainPane.setCenter(buildImageScrollView(filteredImage, false));
              }
              if (newT == null) {
                currentImage.setValue(filteredImage);
              } else {
                switch ((int) newT.getUserData()) {
                  case 0:
                    {
                      filteredImage = currentImage.getValue();
                      currentImage.setValue(originalImage.getValue());
                      break;
                    }
                  case 1:
                    {
                      currentImage.setValue(filteredImage);
                      initSplitScreen();
                      break;
                    }
                }
              }
            });
  }

  private void initSplitScreen() {
    GridPane grid = new GridPane();

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(50);

    ColumnConstraints col2 = new ColumnConstraints();
    col2.setPercentWidth(50);

    RowConstraints row1 = new RowConstraints();
    row1.setVgrow(Priority.ALWAYS);
    row1.setFillHeight(true);

    grid.getColumnConstraints().addAll(col1, col2);
    grid.getRowConstraints().addAll(row1);

    grid.addColumn(0, buildImageScrollView(originalImage.getValue(), true));
    grid.addColumn(1, mainPane.getCenter());

    mainPane.setCenter(grid);
  }

  private void initFilterBar() {
    originalImage.addListener(
        (obs, oldI, newI) -> {
          if (newI != null) filterBar.getChildren().setAll(filterBarManager.buildFilterBar(newI));
        });
  }

  private void initParameterControl() {
    filterBarManager.selectedFilter.addListener(
        (obs, oldF, newF) -> {
          if (newF == null) return;
          filterParameterManager.setCurrentParameters(newF.getParameters());
          selectedFilterName.setText(newF.getFilterName());
          filterParametersContainer
              .getChildren()
              .setAll(filterParameterManager.buildParameterControls());
        });
  }

  private void initAccelerators() {
    KeyCombination kcMinus = new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN);
    KeyCombination kcPlus = new KeyCodeCombination(KeyCode.PLUS, KeyCombination.CONTROL_DOWN);

    Platform.runLater(() -> {
      rootVBox.getScene().getAccelerators().put(kcMinus, this::onZoomOutClick);
      rootVBox.getScene().getAccelerators().put(kcPlus, this::onZoomInClick);
    });
  }

  private void applyNewFilter() {
    currentImage.setValue(
        SwingFXUtils.toFXImage(
            (BufferedImage)
                filterBarManager
                    .selectedFilter
                    .getValue()
                    .apply(
                        SwingFXUtils.fromFXImage(originalImage.getValue(), null),
                        filterParameterManager.getCurrentParameters()),
            null));
    filteredImage = currentImage.getValue();
  }

  private void setNewImage(File file) {
    filterBarManager.reset();
    filterParametersContainer.getChildren().clear();
    Image image = new Image(file.toURI().toString());
    originalImage.setValue(image);
    filteredImage = image;
    currentImage.setValue(image);
  }

  private ScrollPane buildImageScrollView(Image image, boolean isStatic) {
    ImageView imgView = new ImageView(image);
    imgView.scaleXProperty().bind(scaleFactor);
    imgView.scaleYProperty().bind(scaleFactor);
    if (!isStatic) {
      imgView.imageProperty().bind(currentImage);
    }
    StackPane stackPane = new StackPane(new Group(imgView));
    stackPane.setStyle("-fx-background-color: #0F0F0F");
    ScrollPane scrollPane = new ScrollPane(stackPane);
    scrollPane.setStyle("-fx-background-color: #0F0F0F;");
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    return scrollPane;
  }
}
