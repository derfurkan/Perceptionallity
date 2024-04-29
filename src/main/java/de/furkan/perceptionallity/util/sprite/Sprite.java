package de.furkan.perceptionallity.util.sprite;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import lombok.Getter;

@Getter
public class Sprite {

  private Image rawImage;
  private ImageIcon rawImageIcon;

  /**
   * Constructs a Sprite object by loading an image from the specified file and scaling it to the
   * given dimensions. The scaled image is then used to create an ImageIcon.
   *
   * @param resource The file from which the image is loaded.
   * @param dimension The dimensions to which the image is scaled.
   * @throws IOException If there is an error reading the image file.
   */
  public Sprite(File resource, Dimension dimension) {

    try {
      rawImage =
          ImageIO.read(resource)
              .getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
      rawImageIcon = new ImageIcon(rawImage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Sprite(String spriteFile, String... spritePath) {

    try {
      rawImage =
          ImageIO.read(
              Perceptionallity.getGame()
                  .getResourceManager()
                  .getResourceFile(spriteFile, spritePath));
      rawImageIcon = new ImageIcon(rawImage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Sprite(File resource) {

    try {
      rawImage = ImageIO.read(resource);
      rawImageIcon = new ImageIcon(rawImage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Sprite(Image image, Dimension dimension) {
    rawImage = image.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
    rawImageIcon = new ImageIcon(rawImage);
  }

  /**
   * Resizes the image associated with this sprite to the specified dimensions. The resized image is
   * then used to update the ImageIcon.
   *
   * @param dimension The new dimensions to which the image is scaled.
   */
  public void resize(Dimension dimension) {
    rawImage = rawImage.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
    rawImageIcon = new ImageIcon(rawImage);
  }
}
