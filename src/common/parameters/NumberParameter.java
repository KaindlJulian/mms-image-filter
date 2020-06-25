package common.parameters;

import javafx.scene.Node;
import javafx.scene.control.Slider;

import java.util.function.Consumer;

public class NumberParameter extends Parameter {
  private int value;
  private final int defaultValue;
  private final int minValue;
  private final int maxValue;

  public NumberParameter(String name, int defaultValue, int minValue, int maxValue) {
    super(name);
    this.defaultValue = defaultValue;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  public int getValue() {
    if (value < minValue && value > maxValue) {
      return defaultValue;
    }
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getDefaultValue() {
    return defaultValue;
  }

  public int getMinValue() {
    return minValue;
  }

  public int getMaxValue() {
    return maxValue;
  }

  public Node addController(Consumer<Integer> controller) {
    Slider s = new Slider(getMinValue(), getMaxValue(), getDefaultValue());
    s.setShowTickLabels(true);
    s.valueChangingProperty()
        .addListener(
            (obs, oldV, newV) -> {
              if (oldV && !newV) {
                controller.accept((int) s.getValue());
              }
            });
    return s;
  }
}
