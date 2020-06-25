package ui;

import common.Filter;
import filters.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

/** Responsible for all the filters Holds the currently selectedFilter and all the available ones */
public class FilterBarManager {
  /**
   * Observable Object
   *
   * <p>Contains the currently selected filter
   */
  public final ObjectProperty<Filter> selectedFilter = new SimpleObjectProperty<>();

  /** A list of all the available filters */
  private final List<Filter> filters;

  public FilterBarManager() {
    // registering all filters
    this.filters =
        List.of(
            new ThresholdFilter(),
            new GrayscaleFilter(),
            new Anaglyph(),
            new BlueFilter(),
            new BlueRedSwapFilter(),
            new Brightness(),
            new EnhanceColourFilter(),
            new FilterColors(),
            new GreenFilter(),
            new Grid(),
            new MirrorHorizontal(),
            new MirrorVertical(),
            new Negative(),
            new Pride(),
            new RedFilter(),
            new ResizeFilter(),
            new RGBFilter());
  }

  /**
   * Builds UI elements each being a preview for a filter
   *
   * @param original The original unfiltered Image
   * @return A list of filter previews as FX Nodes
   */
  public List<Node> buildFilterBar(Image original) {
    List<Node> filterNodes = new ArrayList<>();

    for (Filter f : filters) {
      StackPane stackPane = new StackPane();
      Image img =
          ImageConverter.toFX(f.apply(ImageConverter.toAwt(original), f.getParametersMap()));
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
          event -> selectedFilter.setValue((Filter) ((StackPane) event.getSource()).getUserData()));
      filterNodes.add(stackPane);
    }

    return filterNodes;
  }

  /** Reset the state */
  public void reset() {
    selectedFilter.setValue(null);
  }
}
