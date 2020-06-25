package common;

import common.parameters.Parameter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Filter {
  String getFilterName();

  Image apply(BufferedImage image, Map<String, Parameter> parameters);

  List<Parameter> getParameters();

  /**
   * @return A map with parameter names as key
   */
  default Map<String, Parameter> getParametersMap() {
    return getParameters().stream()
        .collect(Collectors.toMap(Parameter::getName, Function.identity()));
  }
}
