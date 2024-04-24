package de.furkan.perceptionallity.util.sprite;

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

  public void resize(Dimension dimension) {
    rawImage = rawImage.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
    rawImageIcon = new ImageIcon(rawImage);
  }
}
