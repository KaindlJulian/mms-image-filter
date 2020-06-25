package filters;

import common.NoParamFilter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/** Filter to invert colours to it's negative */
public class Negative implements NoParamFilter {
  @Override
  public String getFilterName() {
    return "Negative";
  }

  @Override
  public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
    BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Pixel p = new Pixel(image.getRGB(j, i));

        // subtract values from 255 to get negative
        bi.setRGB(j, i, Pixel.generateRaw(255 - p.getR(), 255 - p.getG(), 255 - p.getB(), 255));
      }
    }

    return bi;
  }
}
