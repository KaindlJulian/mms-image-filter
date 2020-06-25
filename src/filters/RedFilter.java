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
 * Filter to set Red value to 0 if below certain value
 */
public class RedFilter implements Filter {
    @Override
    public String getFilterName() {
        return "Red filter";
    }

    @Override
    public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int value = ((NumberParameter) parameters.get("red value")).getValue();

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Pixel p = new Pixel(image.getRGB(j, i));

                //check whether red value is below given value
                if (p.getR() < value) {
                    bi.setRGB(j, i, Pixel.generateRaw(0, p.getG(), p.getB(), 255));
                } else {
                    bi.setRGB(j, i, Pixel.generateRaw(p.getR(), p.getG(), p.getB(), 255));
                }
            }
        }

        return bi;
    }

    @Override
    public List<Parameter> getParameters() {
        return List.of(new NumberParameter("red value", 0, 0, 255));
    }
}
