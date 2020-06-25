package ui;

import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

/** Wrapper for SwingFXUtils */
public class ImageConverter {
  static javafx.scene.image.Image toFX(java.awt.Image awtImage) {
    return SwingFXUtils.toFXImage((BufferedImage) awtImage, null);
  }

  static BufferedImage toAwt(javafx.scene.image.Image fxImage) {
    return SwingFXUtils.fromFXImage(fxImage, null);
  }
}
