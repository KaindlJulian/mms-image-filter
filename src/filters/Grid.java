package filters;

import common.Filter;
import common.parameters.NumberParameter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class Grid implements Filter {

  @Override
  public String getFilterName() {
    return "Grid";
  }

  @Override
  public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
    int gridSize = ((NumberParameter) parameters.get("grid size")).getValue();
    BufferedImage bi = new BufferedImage(image.getWidth() + 10, image.getHeight(), image.getType());

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        if (y % gridSize == 0 || x % gridSize == 0) {
          bi.setRGB(x, y, Pixel.generateRaw(0, 0, 0, 0));
        } else {
          bi.setRGB(x, y, image.getRGB(x, y));
        }
      }
    }
    return bi;
  }

  @Override
  public List<Parameter> getParameters() {
    return List.of(new NumberParameter("grid size", 20, 1, 200));
  }
}
