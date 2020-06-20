package filters.utils;

import java.awt.image.ColorModel;

public class Pixel {
  private int raw;
  private static final ColorModel COLOR_MODEL = ColorModel.getRGBdefault();

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
    return COLOR_MODEL.getRed(raw);
  }
  public int getG() {
    return COLOR_MODEL.getGreen(raw);
  }
  public int getB() {
    return COLOR_MODEL.getBlue(raw);
  }
}
