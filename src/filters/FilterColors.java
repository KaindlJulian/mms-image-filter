package filters;

import common.Filter;
import common.parameters.NumberParameter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class FilterColors implements Filter {
  @Override
  public String getFilterName() {
    return "Remove Color";
  }

  @Override
  public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
    int removeColor = ((NumberParameter) parameters.get("remove R/G/B pixels")).getValue();
    BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel p = new Pixel(image.getRGB(x, y));
        if (removeColor == 0) bi.setRGB(x, y, Pixel.generateRaw(0, p.getG(), p.getB(), 255));
        if (removeColor == 1) bi.setRGB(x, y, Pixel.generateRaw(p.getR(), 0, p.getB(), 255));
        if (removeColor == 2) bi.setRGB(x, y, Pixel.generateRaw(p.getR(), p.getG(), 0, 255));
      }
    }
    return bi;
  }

  @Override
  public List<Parameter> getParameters() {
    return List.of(new NumberParameter("remove R/G/B pixels", 1, 0, 2));
  }
}
