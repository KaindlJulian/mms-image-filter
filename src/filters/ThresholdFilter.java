package filters;

import common.Filter;
import common.FilterParameter;
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
  public Image apply(BufferedImage image, Map<String, FilterParameter> params) {
    int threshold = params.get("threshold").getValue(); // key is the parameter name
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
  public List<FilterParameter> getParameters() {
    return List.of(new FilterParameter("threshold", 128, 0, 255));
  }
}
