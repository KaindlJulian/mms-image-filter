package filters.utils;

import java.awt.image.ColorModel;

public class Pixel {
  private int raw;
  private ColorModel colorModel = ColorModel.getRGBdefault();


  public Pixel(int raw) {
    this.raw = raw;
  }

  public static int generateRaw(int r, int g, int b, int a) {
    a = a << 24;
    r = r << 16;
    g = g << 8;
    return a | r | g | b;
  }

  public int getR() {
    return colorModel.getRed(raw);
  }
  public int getG() {
    return colorModel.getGreen(raw);
  }
  public int getB() {
    return colorModel.getBlue(raw);
  }
}
