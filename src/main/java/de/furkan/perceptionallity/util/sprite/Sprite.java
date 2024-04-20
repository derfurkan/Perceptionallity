package de.furkan.perceptionallity.util.sprite;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import lombok.Getter;

@Getter
public class Sprite {

  private final JLabel rawComponent;
  private Image rawImage;
  private ImageIcon rawImageIcon;

  protected Sprite(File resource, Dimension dimension) {

    try {
      rawImage =
          ImageIO.read(resource).getScaledInstance(dimension.width,dimension.height,Image.SCALE_SMOOTH);
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.rawComponent = new JLabel();
    this.rawComponent.setIcon(rawImageIcon = new ImageIcon(this.rawImage));
    this.rawComponent.setBounds(0, 0, dimension.width, dimension.height);
  }

  public void resize(Dimension dimension) {
    this.rawImage = this.rawImage.getScaledInstance(dimension.width,dimension.height,Image.SCALE_SMOOTH);
    this.rawImageIcon = new ImageIcon(this.rawImage);
    this.rawComponent.setIcon(this.rawImageIcon);
    this.rawComponent.setBounds(0, 0, dimension.width, dimension.height);
  }

}
