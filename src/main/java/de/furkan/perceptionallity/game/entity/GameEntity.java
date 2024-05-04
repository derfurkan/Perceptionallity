package de.furkan.perceptionallity.game.entity;

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
      Dimension dimension,
      WorldLocation worldLocation,
      int maxHealth,
      int currentHealth,
      int moveSpeed,boolean passToCollisionCheck) {
    super(dimension, worldLocation, passToCollisionCheck);
    this.maxHealth = maxHealth;
    this.moveSpeed = moveSpeed;
    this.currentHealth = currentHealth;
    this.jLabel = new JLabel();
    this.gameEntityObject = this;
    setComponent(jLabel);
  }
  
}
