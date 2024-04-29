package de.furkan.perceptionallity.game.entity;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.GameObject;
import de.furkan.perceptionallity.game.WorldLocation;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameEntity extends GameObject {

  private GameEntity gameEntityObject;
  private int maxHealth, currentHealth, moveSpeed;
  private JLabel jLabel;

  public GameEntity(
      Rectangle rectangle,
      WorldLocation worldLocation,
      int maxHealth,
      int currentHealth,
      int moveSpeed) {
    super(rectangle, worldLocation);
    this.maxHealth = maxHealth;
    this.moveSpeed = moveSpeed;
    this.currentHealth = currentHealth;
    this.jLabel = new JLabel();
    this.gameEntityObject = this;
    setComponent(jLabel);
  }

  @Override
  public void buildGameObject() {
    Perceptionallity.getGame().getGameManager().registerGameObject(this);
    jLabel.setBounds(getRectangle());
    Perceptionallity.getGame().getGamePanel().add(getComponent(), 1);
  }
}
