package de.furkan.perceptionallity.game.entity;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.WorldLocation;
import de.furkan.perceptionallity.game.GameObject;
import de.furkan.perceptionallity.util.sprite.Sprite;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Setter
@Getter
public class GameEntity extends GameObject {

  private int maxHealth, currentHealth;

  private Sprite initialSprite;
  private JLabel jLabel;

  public GameEntity(
      Rectangle rectangle,
      WorldLocation worldLocation,
      Sprite initialSprite,
      int maxHealth,
      int currentHealth,
      String entityName) {
    super(rectangle, worldLocation);
    this.initialSprite = initialSprite;
    this.maxHealth = maxHealth;
    this.currentHealth = currentHealth;
    this.jLabel = new JLabel(entityName);
    setComponent(jLabel);
  }

  @Override
  public void buildGameObject() {
    Perceptionallity.getGame().getGameManager().registerGameObject(this);
    initialSprite.resize(getRectangle().getSize());
    jLabel.setIcon(initialSprite.getRawImageIcon());
    jLabel.setBounds(getRectangle());
    Perceptionallity.getGame().getGamePanel().add(getComponent(), 1);
  }
}
