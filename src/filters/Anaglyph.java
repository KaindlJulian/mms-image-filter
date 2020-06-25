package filters;

import common.Filter;
import common.parameters.NumberParameter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class Anaglyph implements Filter {
  @Override
  public String getFilterName() {
    return "Anaglyph";
  }

  @Override
  public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
    int rate = ((NumberParameter) parameters.get("rate")).getValue();
    BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth() - rate; j++) {
        Pixel pR = new Pixel(image.getRGB(j, i));
        Pixel pL = new Pixel(image.getRGB(j + rate, i));

        bi.setRGB(j, i, Pixel.generateRaw(pL.getR(), pR.getG(), pR.getB(), 255));
      }
    }
    return bi;
  }

  @Override
  public List<Parameter> getParameters() {
    return List.of(new NumberParameter("rate", 10, 0, 50));
  }
}
