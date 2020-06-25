package filters;

import common.NoParamFilter;
import common.parameters.Parameter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class MirrorVertical implements NoParamFilter {
  @Override
  public String getFilterName() {
    return "Mirror Vertical";
  }

  @Override
  public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
    BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        bi.setRGB(x, image.getHeight() - 1 - y, image.getRGB(x, y));
      }
    }

    return bi;
  }
}
