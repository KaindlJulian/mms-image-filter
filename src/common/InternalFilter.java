package common;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface InternalFilter  {
    Image apply(BufferedImage image);
}
