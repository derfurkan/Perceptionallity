package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import lombok.Getter;

@Getter
public class GameSprite extends GameObject {

  private final Sprite sprite;
  private float alpha = 1.0f;

  public GameSprite(Dimension dimension, WorldLocation worldLocation, Sprite sprite) {
    super(dimension, worldLocation, false);
    this.sprite = sprite;
    sprite.resize(dimension);
    getComponent().setIcon(sprite.getRawImageIcon());
  }

  public float getOpacity() {
    return alpha;
  }

  public void setOpacity(float opacity) {
    this.alpha = opacity;
    BufferedImage bufferedImage =
        new BufferedImage(
            sprite.getRawImage().getWidth(null),
            sprite.getRawImage().getHeight(null),
            BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = bufferedImage.createGraphics();
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
    g2d.drawImage(sprite.getRawImage(), 0, 0, null);
    g2d.dispose();

    getComponent().setIcon(new ImageIcon(bufferedImage));
  }
}
