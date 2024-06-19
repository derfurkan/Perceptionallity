package de.furkan.perceptionallity.menu.components.sprite;

import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.menu.components.MenuComponent;
import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

public class MenuSprite extends MenuComponent {

  private final JLabel rawComponent;
  @Setter @Getter private Sprite sprite;
  @Setter @Getter private Animation spriteAnimation;

  public MenuSprite(int x, int y, Dimension dimension, Sprite sprite) {
    super(x, y, dimension);
    this.rawComponent = new JLabel();

    if (sprite != null) {
      this.sprite = sprite;
      sprite.resize(dimension);
      rawComponent.setIcon(sprite.getRawImageIcon());
    }
  }

  public void displayNextAnimationFrame() {
    if (spriteAnimation == null) return;

    spriteAnimation.nextFrame();
    setSprite(spriteAnimation.getCurrentFrame());
    rawComponent.setIcon(spriteAnimation.getCurrentFrame().getRawImageIcon());
  }

  @Override
  public JComponent getJComponent() {
    return rawComponent;
  }
}
