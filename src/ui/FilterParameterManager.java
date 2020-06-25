package ui;

import common.parameters.NumberParameter;
import common.parameters.Parameter;
import common.parameters.TextParameter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Responsible for the parameters of a selected filter. */
public class FilterParameterManager {

  /** A list of the currently displayed parameters */
  private List<Parameter> currentParameters;

  /**
   * Observable boolean
   *
   * <p>Indicates when a parameter changed
   */
  public BooleanProperty changed = new SimpleBooleanProperty();

  public FilterParameterManager() {
    this.currentParameters = new ArrayList<>();
  }

  public void setCurrentParameters(List<Parameter> params) {
    currentParameters = params;
  }

  public Map<String, Parameter> getCurrentParameters() {
    return currentParameters.stream()
        .collect(Collectors.toMap(Parameter::getName, Function.identity()));
  }

  /**
   * Build Controls for the parameters and bind them to the current parameters and change property
   *
   * @return A list of UI elements each being a control element for a filter parameter
   */
  public List<Node> buildParameterControls() {
    ArrayList<Node> controls = new ArrayList<>();

    for (Parameter p : currentParameters) {
      Label l = new Label(p.getName());
      l.setPadding(new Insets(0, 0, 16, 0));
      controls.add(l);
      Node toAdd = null;

      if (p instanceof NumberParameter) {
        Slider s =
            new Slider(
                ((NumberParameter) p).getMinValue(),
                ((NumberParameter) p).getMaxValue(),
                ((NumberParameter) p).getDefaultValue());
        s.setShowTickLabels(true);
        s.valueChangingProperty()
            .addListener(
                (obs, oldV, newV) -> {
                  if (oldV && !newV) {
                    ((NumberParameter) p).setValue((int) s.getValue());
                    changed.setValue(!changed.getValue());
                  }
                });

        toAdd = s;
      }

      if (p instanceof TextParameter) {
        TextArea tf = new TextArea(((TextParameter) p).getDefaultValue());
        tf.textProperty()
            .addListener(
                (obs, oldT, newT) -> {
                  ((TextParameter) p).setValue(newT);
                  changed.setValue(!changed.getValue());
                });
        toAdd = tf;
      }

      controls.add(toAdd);
    }

    return controls;
  }
}
