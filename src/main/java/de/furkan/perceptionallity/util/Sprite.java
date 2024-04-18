package de.furkan.perceptionallity.util;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import lombok.Getter;

@Getter
public class Sprite {

  private final JLabel rawComponent;
  private Image rawImage;

  protected Sprite(URL resource, Dimension dimension, int scaling) {

    try {
      rawImage =
          ImageIO.read(resource).getScaledInstance(dimension.width, dimension.height, scaling);
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.rawComponent = new JLabel();
    this.rawComponent.setIcon(new ImageIcon(this.rawImage));
    this.rawComponent.setBounds(0, 0, dimension.width, dimension.height);
  }
}
