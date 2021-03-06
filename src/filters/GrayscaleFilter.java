package filters;

import common.NoParamFilter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class GrayscaleFilter implements NoParamFilter {
  @Override
  public String getFilterName() {
    return "Grayscale";
  }

  @Override
  public Image apply(BufferedImage image, Map<String, Parameter> params) {
    BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Pixel p = new Pixel(image.getRGB(j, i));
        int avg = (p.getR() + p.getG() + p.getB()) / 3;
        bi.setRGB(j, i, Pixel.generateRaw(avg, avg, avg, 255));
      }
    }

    return bi;
  }
}
