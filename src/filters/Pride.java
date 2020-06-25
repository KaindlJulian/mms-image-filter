package filters;

import common.Filter;
import common.NoParamFilter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class Pride implements NoParamFilter {
  @Override
  public String getFilterName() {
    return "Rainbow Pride";
  }

  @Override
  public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
    BufferedImage bi = new BufferedImage(image.getWidth() + 10, image.getHeight(), image.getType());

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        Pixel p = new Pixel(image.getRGB(x, y));
        if (y < image.getHeight() / 6) { // first stripe = red
          bi.setRGB(x, y, Pixel.generateRaw(p.getR(), 0, 0, 255));
        } else if (y >= image.getHeight() / 6
            && y < image.getHeight() / 3) { // second stripe = orange
          bi.setRGB(x, y, Pixel.generateRaw(p.getR(), p.getG() / 2, 0, 30));
        } else if (y >= image.getHeight() / 3
            && y < image.getHeight() / 2) { // third stripe = yellow
          bi.setRGB(x, y, Pixel.generateRaw(p.getR(), p.getG(), 0, 255));
        } else if (y >= image.getHeight() / 2
            && y < image.getHeight() / 2 + image.getHeight() / 6) { // fourth stripe = green
          bi.setRGB(x, y, Pixel.generateRaw(0, p.getG(), 0, 255));
        } else if (y >= image.getHeight() / 2 + image.getHeight() / 6
            && y < image.getHeight() / 2 + image.getHeight() / 3) { // fifth stripe = blue
          bi.setRGB(x, y, Pixel.generateRaw(0, 0, p.getB(), 100));
        } else if (y >= image.getHeight() / 2 + image.getHeight() / 3
            && y < image.getHeight()) { // sixth stripe = violet
          bi.setRGB(x, y, Pixel.generateRaw(p.getR(), 0, p.getB(), 100));
        } else bi.setRGB(x, y, image.getRGB(x, y));
      }
    }
    return bi;
  }
}
