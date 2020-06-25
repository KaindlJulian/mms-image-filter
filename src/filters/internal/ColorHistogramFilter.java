package filters.internal;

import common.InternalFilter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ColorHistogramFilter implements InternalFilter {

  @Override
  public Image apply(BufferedImage image) {

    Map<Integer, Integer> red = new HashMap<>();
    Map<Integer, Integer> green = new HashMap<>();
    Map<Integer, Integer> blue = new HashMap<>();

    IntStream.range(0, 256)
        .forEach(
            i -> {
              red.put(i, 0);
              green.put(i, 0);
              blue.put(i, 0);
            });

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Pixel p = new Pixel(image.getRGB(j, i));
        int[] rgb = p.getRGBArray();

        red.merge(rgb[0], 1, Integer::sum);
        green.merge(rgb[1], 1, Integer::sum);
        blue.merge(rgb[2], 1, Integer::sum);
      }
    }

    int redMaxFreq = red.values().stream().max(Comparator.comparingInt(v -> v)).get();
    int greenMaxFreq = green.values().stream().max(Comparator.comparingInt(v -> v)).get();
    int blueMaxFreq = blue.values().stream().max(Comparator.comparingInt(v -> v)).get();

    int freqBound = Math.min(Math.min(redMaxFreq, blueMaxFreq), greenMaxFreq);

    BufferedImage histogram = new BufferedImage(256, 122, BufferedImage.TYPE_INT_ARGB);

    for (int i = 0; i < histogram.getWidth(); i++) {
      for (int j = 0; j < histogram.getHeight(); j++) {
        int r = 0;
        int g = 0;
        int b = 0;

        if ((121 - j) < red.get(i) * (121.0 / freqBound) && red.get(i) != 0) {
          r = 255;
        }
        if ((121 - j) < green.get(i) * (121.0 / freqBound) && green.get(i) != 0) {
          g = 255;
        }
        if ((121 - j) < blue.get(i) * (121.0 / freqBound) && blue.get(i) != 0) {
          b = 255;
        }

        if (!(r == 0 && g == 0 && b == 0)) {
          histogram.setRGB(i, j, Pixel.generateRaw(r, g, b, 255));
        }
      }
    }

    return histogram;
  }
}
