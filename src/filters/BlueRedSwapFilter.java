package filters;

import common.NoParamFilter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Filter to swap out Red and Blue values of every pixel of a given image
 */
public class BlueRedSwapFilter implements NoParamFilter {
    @Override
    public String getFilterName() {
        return "Blue Red Swap";
    }

    @Override
    public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Pixel p = new Pixel(image.getRGB(j, i));

                //swap blue and red values
                bi.setRGB(j, i, Pixel.generateRaw(p.getB(), p.getG(), p.getR(), 255));
            }
        }

        return bi;
    }
}
