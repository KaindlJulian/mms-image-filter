package filters;

import common.Filter;
import common.parameters.NumberParameter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/** Filter to set Green value to 0 if below certain value */
public class GreenFilter implements Filter {
  @Override
  public String getFilterName() {
    return "Green Filter";
  }

  @Override
  public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
    BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    int value = ((NumberParameter) parameters.get("green value")).getValue();

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Pixel p = new Pixel(image.getRGB(j, i));

        // check whether green value is below given value
        if (p.getG() < value) {
          bi.setRGB(j, i, Pixel.generateRaw(p.getR(), 0, p.getB(), 255));
        } else {
          bi.setRGB(j, i, Pixel.generateRaw(p.getR(), p.getG(), p.getB(), 255));
        }
      }
    }

    return bi;
  }

  @Override
  public List<Parameter> getParameters() {
    return List.of(new NumberParameter("green value", 0, 0, 255));
  }
}
