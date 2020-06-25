package filters;

import common.Filter;
import common.parameters.NumberParameter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class ThresholdFilter implements Filter {
  @Override
  public String getFilterName() {
    return "Threshold";
  }

  @Override
  public Image apply(BufferedImage image, Map<String, Parameter> params) {
    int threshold = ((NumberParameter) params.get("threshold")).getValue();
    BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Pixel p = new Pixel(image.getRGB(j, i));
        int avg = (p.getR() + p.getG() + p.getB()) / 3;
        if (avg > threshold) {
          bi.setRGB(j, i, Pixel.generateRaw(255, 255, 255, 255));
        } else {
          bi.setRGB(j, i, Pixel.generateRaw(0, 0, 0, 255));
        }
      }
    }

    return bi;
  }

  @Override
  public List<Parameter> getParameters() {
    return List.of(new NumberParameter("threshold", 128, 0, 255));
  }
}
