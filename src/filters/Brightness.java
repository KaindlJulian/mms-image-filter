package filters;

import common.Filter;
import common.parameters.NumberParameter;
import common.parameters.Parameter;
import filters.utils.Pixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Filter to increase or decrease the brightness of the picture
 */
public class Brightness implements Filter {
    @Override
    public String getFilterName() {
        return "Brightness";
    }

    @Override
    public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int brightness = ((NumberParameter) parameters.get("brightness")).getValue();

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Pixel p = new Pixel(image.getRGB(j, i));

                int r = p.getR() + brightness;
                int g = p.getG() + brightness;
                int b = p.getB() + brightness;

                //check if RGB-Values are still within the range of 0 - 255
                if (r < 0) r = 0;
                if (r > 255) r = 255;
                if (g < 0) g = 0;
                if (g > 255) g = 255;
                if (b < 0) b = 0;
                if (b > 255) b = 255;

                bi.setRGB(j, i, Pixel.generateRaw(r, g, b, 255));
            }
        }

        return bi;

    }


    @Override
    public List<Parameter> getParameters() {
        return List.of(new NumberParameter("brightness", 0, -255, 255));
    }
}
