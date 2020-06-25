package common.parameters;

public class TextParameter extends Parameter {
  private String value;
  private final String defaultValue;

  public TextParameter(String name, String defaultValue) {
    super(name);
    this.defaultValue = defaultValue;
  }

  public String getValue() {
    return value == null ? defaultValue : value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDefaultValue() {
    return defaultValue;
  }
}
