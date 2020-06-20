package common;

public class FilterParameter {
  private final String name;
  private int value;
  private final int defaultValue;
  private final double minValue;
  private final double maxValue;

  public FilterParameter(String name, int defaultValue, int minValue, int maxValue) {
    this.name = name;
    this.defaultValue = defaultValue;
    this.minValue = minValue;
    this.maxValue = maxValue;
    setValue(defaultValue);
  }

  public String getName() {
    return name;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getDefaultValue() {
    return defaultValue;
  }

  public double getMinValue() {
    return minValue;
  }

  public double getMaxValue() {
    return maxValue;
  }
}
