package ui;

import common.FilterParameter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Responsible for the parameters of a selected filter. */
public class FilterParameterManager {

  /** A list of the currently displayed parameters */
  private List<FilterParameter> currentParameters;

  /**
   * Observable boolean
   *
   * <p>Indicates when a parameter changed
   */
  public BooleanProperty changed = new SimpleBooleanProperty();

  public FilterParameterManager() {
    this.currentParameters = new ArrayList<>();
  }

  public void setCurrentParameters(List<FilterParameter> params) {
    currentParameters = params;
  }

  public Map<String, FilterParameter> getCurrentParameters() {
    return currentParameters.stream()
            .collect(Collectors.toMap(FilterParameter::getName, Function.identity()));
  }

  /**
   * Build Controls for the parameters and bind them to the current parameters and change property
   * @return A list of UI elements each being a control element for a filter parameter
   */
  public List<Node> buildParameterControls() {
    ArrayList<Node> controls = new ArrayList<>();

    for (FilterParameter p : currentParameters) {
      Label l = new Label(p.getName());
      l.setPadding(new Insets(0, 0, 16, 0));
      controls.add(l);

      Slider s = new Slider(p.getMinValue(), p.getMaxValue(), p.getDefaultValue());
      s.setShowTickLabels(true);
      s.valueChangingProperty()
          .addListener(
              (obs, oldV, newV) -> {
                if (oldV && !newV) {
                  p.setValue((int) s.getValue());
                  changed.setValue(!changed.getValue());
                }
              });
      controls.add(s);
    }

    return controls;
  }
}
