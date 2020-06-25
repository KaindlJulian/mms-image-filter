package common.parameters;

import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

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

  public Node addController(Consumer<String> controller) {
    TextField textField = new TextField(defaultValue);
    textField.textProperty().addListener((obs, oldT, newT) -> controller.accept(newT));
    return textField;
  }
}
