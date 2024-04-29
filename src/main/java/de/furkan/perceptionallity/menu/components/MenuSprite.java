package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

public class MenuSprite extends MenuComponent {

  @Getter private final Sprite sprite;
  private final JLabel rawComponent;

  public MenuSprite(int x, int y, Dimension dimension, Sprite sprite) {
    super(x, y, dimension);
    this.sprite = sprite;
    sprite.resize(dimension);
    this.rawComponent = new JLabel(sprite.getRawImageIcon());
  }

  @Override
  public JComponent getJComponent() {
    return rawComponent;
  }
}
