package common;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Filter {
  String getFilterName();

  Image apply(BufferedImage image, Map<String, FilterParameter> parameters);

  List<FilterParameter> getParameters();

  /**
   * @return A map with parameter names as key
   */
  default Map<String, FilterParameter> getParametersMap() {
    return getParameters().stream()
        .collect(Collectors.toMap(FilterParameter::getName, Function.identity()));
  }
}
