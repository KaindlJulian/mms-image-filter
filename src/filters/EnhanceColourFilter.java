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
 * This Filter enhances the most prominent colour in every pixel
 */
public class EnhanceColourFilter implements Filter {
    @Override
    public String getFilterName() {
        return "Enhance Colour Filter";
    }

    @Override
    public Image apply(BufferedImage image, Map<String, Parameter> parameters) {
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int val = ((NumberParameter) parameters.get("value")).getValue();

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Pixel p = new Pixel(image.getRGB(j, i));

                //enhance the most prominent colour by value
                switch (mainColour(p)) {
                    case 'r':
                        bi.setRGB(j, i, Pixel.generateRaw(Math.min(p.getR() + val, 255), p.getG(), p.getB(), 255));
                        break;
                    case 'g':
                        bi.setRGB(j, i, Pixel.generateRaw(p.getR(), Math.min(p.getG() + val, 255), p.getB(), 255));
                        break;
                    case 'b':
                        bi.setRGB(j, i, Pixel.generateRaw(p.getR() , p.getG(), Math.min(p.getB() + val, 255), 255));
                        break;
                    default:
                        bi.setRGB(j, i, Pixel.generateRaw(p.getR(), p.getG(), p.getB(), 255));
                        break;
                }
            }
        }
        return bi;
    }

    //method to determine which RGB-value is the highest
    private char mainColour(Pixel pixel) {

        if (pixel.getR() >= pixel.getG() && pixel.getR() >= pixel.getB()) {
            return 'r';
        }
        if (pixel.getG() >= pixel.getR() && pixel.getG() >= pixel.getB()) {
            return 'g';
        }
        if (pixel.getB() >= pixel.getR() && pixel.getB() >= pixel.getG()) {
            return 'b';
        }
        return 0;

    }

    @Override
    public List<Parameter> getParameters() {
        return List.of(new NumberParameter("value", 0, 0, 100));
    }
}
