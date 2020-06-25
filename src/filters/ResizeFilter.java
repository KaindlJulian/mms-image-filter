package filters;

import common.Filter;
import common.parameters.NumberParameter;
import common.parameters.Parameter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Filter to resize a given image
 */
public class ResizeFilter implements Filter {
    @Override
    public String getFilterName() {
        return "Resize";
    }

    @Override
    public Image apply(BufferedImage image, Map<String, Parameter> parameters) {

        //calculate the new size based on given percentage
        float size = (float) ((NumberParameter) parameters.get("size")).getValue() / 100;
        int scaledWidth = (int) (image.getWidth() * size );
        int scaledHeight = (int) (image.getHeight() * size);

        BufferedImage bi = new BufferedImage(scaledWidth, scaledHeight, image.getType());
        Graphics2D g = bi.createGraphics();
        g.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();

        return bi;
    }

    @Override
    public List<Parameter> getParameters() {
        return List.of(new NumberParameter("size", 100, 10, 200));
    }
}
