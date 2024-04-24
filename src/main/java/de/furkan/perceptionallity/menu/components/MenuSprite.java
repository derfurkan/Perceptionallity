package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;
import javax.swing.*;

public class MenuSprite extends MenuComponent {

  private final Sprite sprite;

  public MenuSprite(Sprite sprite) {
    super(
        sprite.getRawComponent().getX(),
        sprite.getRawComponent().getY(),
        new Dimension(sprite.getRawComponent().getWidth(), sprite.getRawComponent().getHeight()));
    this.sprite = sprite;
  }

  @Override
  public JComponent getJComponent() {
    return sprite.getRawComponent();
  }
}
