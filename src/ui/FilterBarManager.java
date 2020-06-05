package ui;

import common.Filter;
import filters.GrayscaleFilter;
import filters.ThresholdFilter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FilterBarManager {
  private final List<Filter> filters;
  public final ObjectProperty<Filter> selectedFilter = new SimpleObjectProperty<Filter>();

  public FilterBarManager() {
    this.filters = List.of(new ThresholdFilter(), new GrayscaleFilter());
  }

  public List<Node> buildFilterBar(Image original) {
    List<Node> filterNodes = new ArrayList<>();

    for (Filter f : filters) {
      StackPane stackPane = new StackPane();
      Image img =
          SwingFXUtils.toFXImage(
              (BufferedImage)
                  f.apply(SwingFXUtils.fromFXImage(original, null), f.getParametersMap()),
              null);
      ImageView imgView = new ImageView(img);

      imgView.setPreserveRatio(true);
      imgView.setFitHeight(114);
      imgView.setFitWidth(202);
      Label label = new Label(f.getFilterName());
      label.prefHeightProperty().bind(stackPane.heightProperty());
      label.prefWidthProperty().bind(stackPane.widthProperty());
      label.setAlignment(Pos.BOTTOM_LEFT);
      label.setPadding(new Insets(0, 0, 4, 4));
      stackPane.getChildren().addAll(imgView, label);
      stackPane.setUserData(f);
      stackPane.setOnMouseClicked(
          event -> {
            selectedFilter.setValue((Filter) ((StackPane) event.getSource()).getUserData());
          });
      filterNodes.add(stackPane);
    }

    return filterNodes;
  }

  public void reset() {
    selectedFilter.setValue(null);
  }
}
