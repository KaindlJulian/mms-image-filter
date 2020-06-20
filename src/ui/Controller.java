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

/**
 * Controller of the JavaFX MVC Pattern
 *
 * <p>Handles user input and dynamically changing UI Elements
 */
public class Controller {

  /** Default zoom in/out amount in percent[%] */
  private final int ZOOM_AMOUNT = 20;

  /**
   * Observable Double
   *
   * <p>Contains the current image scaleFactor. Used to zoom in/out. 1 = 100%.
   */
  private final DoubleProperty scaleFactor = new SimpleDoubleProperty(1);

  /**
   * Observable Object
   *
   * <p>Contains the currently displayed image. If no filter was applied this contains the
   * originalImage and otherwise the filtered one.
   */
  private final ObjectProperty<Image> currentImage = new SimpleObjectProperty<>();

  /**
   * Observable Object
   *
   * <p>Contains the original image. Only updated when a new image is imported.
   */
  private final ObjectProperty<Image> originalImage = new SimpleObjectProperty<>();

  /** Helper variable to always contain the result of the most recent filter application */
  private Image filteredImage;

  /**
   * UIManager, controls the bottom filter bar
   *
   * <p>Holds the currently selected and all applicable Filters. Notifies listeners when the
   * selected Filter changed.
   */
  private final FilterBarManager filterBarManager = new FilterBarManager();

  /**
   * UIManager, controls the filter parameters
   *
   * <p>Holds the currently selected filter parameters. Notifies listeners when one of the filter
   * parameters changed in value.
   */
  private final FilterParameterManager filterParameterManager = new FilterParameterManager();

  /** The ImportFileChooser is instanced here, because it needs special setup */
  private final FileChooser importFileChooser = new FileChooser();

  /** Reference to the root VBox */
  @FXML private VBox rootVBox;

  /** Reference to the ImageView */
  @FXML private ImageView imageView;

  /** Reference to the zoom ComboBox */
  @FXML private ComboBox<String> zoomComboBox;

  /** Reference to the file drop info panel (to hide/show it) */
  @FXML private StackPane dropAreaInformation;

  /** Reference to the toggleOriginal ToggleButton */
  @FXML private ToggleButton toggleOriginal;

  /** Reference to the toggleSplitScreen ToggleButton */
  @FXML private ToggleButton toggleSplitScreen;

  /** Reference to the main BorderPane */
  @FXML private BorderPane mainPane;

  /** Reference to the filterBar containing all the applicable filters */
  @FXML private HBox filterBar;

  /** Reference to the selectedFilterName Label */
  @FXML private Label selectedFilterName;

  /** Reference to the filterParametersContainer */
  @FXML private VBox filterParametersContainer;

  /** Initializes the UI */
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

  /** Handles a click on the import button */
  @FXML
  private void onImportClick() {
    try {
      File file = importFileChooser.showOpenDialog(rootVBox.getScene().getWindow());
      setNewImage(file);
    } catch (NullPointerException exception) {
      System.out.println("Dialog aborted!");
    }
  }

  /** Handles a click on the export button */
  @FXML
  private void onExportClick() {
    if (currentImage.getValue() != null) {
      BufferedImage bi = SwingFXUtils.fromFXImage(currentImage.getValue(), null);
      FileChooser fc = new FileChooser();
      File outFile = fc.showSaveDialog(rootVBox.getScene().getWindow());

      try {
        ImageIO.write(bi, "png", outFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** * Handles a click on the zoom in button */
  @FXML
  private void onZoomInClick() {
    int newZoom = Integer.parseInt(zoomComboBox.getValue().replace("%", "")) + ZOOM_AMOUNT;
    zoomComboBox.valueProperty().setValue(newZoom + "%");
  }

  /** Handles a click on the zoom out button */
  @FXML
  private void onZoomOutClick() {
    int newZoom = Integer.parseInt(zoomComboBox.getValue().replace("%", "")) - ZOOM_AMOUNT;
    zoomComboBox.valueProperty().setValue(newZoom + "%");
  }

  /**
   * Handles dropped files
   *
   * @param event A DragEvent
   */
  @FXML
  private void onFileDrop(DragEvent event) {
    Dragboard dragboard = event.getDragboard();
    if (dragboard.hasFiles()) {
      setNewImage(dragboard.getFiles().get(0));
    }
    dragboard.clear();
  }

  /** Initializes the zoom combo box listener */
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

  /** Initializes all file choosers */
  private void initFileChoosers() {
    File home = new File(System.getProperty("user.home"));
    FileChooser.ExtensionFilter imageFilter =
        new FileChooser.ExtensionFilter("image", "*jpg", "*.jpeg", "*.png");

    importFileChooser.setInitialDirectory(home);
    importFileChooser.setTitle("Select Image");
    importFileChooser.getExtensionFilters().add(imageFilter);
  }

  /** Initializes the toggle between display modes */
  private void initDisplayModeToggle() {
    ToggleGroup displayMode = new ToggleGroup();
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

  /** Initializes a split screen view */
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

  /** Initializes the filter bar listener */
  private void initFilterBar() {
    originalImage.addListener(
        (obs, oldI, newI) -> {
          if (newI != null) filterBar.getChildren().setAll(filterBarManager.buildFilterBar(newI));
        });
  }

  /** Initializes the parameter control listener */
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

  /** Initializes the keyboard shortcuts to zoom in/out */
  private void initAccelerators() {
    KeyCombination kcMinus = new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN);
    KeyCombination kcPlus = new KeyCodeCombination(KeyCode.PLUS, KeyCombination.CONTROL_DOWN);

    Platform.runLater(
        () -> {
          rootVBox.getScene().getAccelerators().put(kcMinus, this::onZoomOutClick);
          rootVBox.getScene().getAccelerators().put(kcPlus, this::onZoomInClick);
        });
  }

  /** Applies the selected filter from the {@link #filterBarManager} */
  private void applyNewFilter() {
    java.awt.Image filterResult =
        filterBarManager
            .selectedFilter
            .getValue()
            .apply(
                ImageConverter.toAwt(originalImage.getValue()),
                filterParameterManager.getCurrentParameters());

    currentImage.setValue(ImageConverter.toFX(filterResult));
    filteredImage = currentImage.getValue();
  }

  /**
   * Updates the {@link #currentImage} and {@link #originalImage}. Also resets the {@link
   * #filterBarManager} and UI.
   *
   * @param file A file containing the image
   */
  private void setNewImage(File file) {
    filterBarManager.reset();
    filterParametersContainer.getChildren().clear();
    Image image = new Image(file.toURI().toString());
    originalImage.setValue(image);
    filteredImage = image;
    currentImage.setValue(image);
  }

  /**
   * Centers the image inside of a ScrollPane
   *
   * @param image The image to display
   * @param isStatic Bind the imageView image property to the {@link #currentImage}
   * @return The prepared ScrollPane
   */
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
