package filters;

import common.Filter;
import common.parameters.NumberParameter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/** Filter to reduce colours to either red, green or blue */
public class RGBFilter implements Filter {

  @Override
  public String getFilterName() {
    return "RGB Filter";
  }

  @Override
  public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
    BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    int rgb = ((NumberParameter) parameters.get("RGB")).getValue();

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Pixel p = new Pixel(image.getRGB(j, i));

        // check which colour is selected
        if (rgb == 0) {
          bi.setRGB(
              j,
              i,
              Pixel.generateRaw(
                  Math.max((p.getR() - p.getB()) + (p.getR() - p.getG()), 0), 0, 0, 255));
        }
        if (rgb == 1) {
          bi.setRGB(
              j,
              i,
              Pixel.generateRaw(
                  0, Math.max((p.getG() - p.getR()) + (p.getG() - p.getB()), 0), 0, 255));
        }
        if (rgb == 2) {
          bi.setRGB(
              j,
              i,
              Pixel.generateRaw(
                  0, 0, Math.max((p.getB() - p.getR()) + (p.getB() - p.getG()), 0), 255));
        }
      }
    }

    return bi;
  }

  @Override
  public List<Parameter> getParameters() {
    return List.of(new NumberParameter("RGB", 0, 0, 2));
  }
}
