package common;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public interface InternalFilter  {
    Image apply(BufferedImage image);
}
