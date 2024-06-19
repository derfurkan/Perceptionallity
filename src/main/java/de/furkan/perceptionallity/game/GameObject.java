package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Game;
import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.game.entity.npc.GameNPC;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class GameObject {

  private final WorldLocation worldLocation, lastLocation = new WorldLocation(0, 0);
  private final Dimension dimension;
  private final boolean passToCollisionCheck;
  private final GameVelocity currentVelocity;
  private final JLabel component = new JLabel();
  @Setter private Dimension collisionBoundaries;
  @Setter private GameAction onCollision;
  private Animation currentPlayingAnimation;
  private Animation lastPlayedAnimation;
  private int objectLayer;

  public GameObject(
      Dimension dimension, WorldLocation worldLocation, boolean passToCollisionCheck) {
    this.worldLocation = worldLocation;
    this.passToCollisionCheck = passToCollisionCheck;
    this.currentVelocity = new GameVelocity(0, 0);
    this.dimension = dimension;
  }

  public void playAnimation(Animation animation) throws CloneNotSupportedException {
    if (currentPlayingAnimation != null) lastPlayedAnimation = currentPlayingAnimation.clone();
    currentPlayingAnimation = animation.clone();
    currentPlayingAnimation.resizeTo(getDimension());
  }

  public void playAnimation(Animation animation, int fps) throws CloneNotSupportedException {
    if (currentPlayingAnimation != null) lastPlayedAnimation = currentPlayingAnimation.clone();
    currentPlayingAnimation = animation.clone();
    currentPlayingAnimation.resizeTo(getDimension());
    currentPlayingAnimation.setFramesPerSecond(fps);
  }

  public Rectangle buildRectangle() {
    return new Rectangle(
        worldLocation.getX(), worldLocation.getY(), dimension.width, dimension.height);
  }

  public void initializeGameObject(int layer) {
    Perceptionallity.getGame().getGameManager().registerGameObject(this);
    if (this instanceof GameNPC)
      Perceptionallity.getGame().getGameManager().registerNPC((GameNPC) this);
    component.setBounds(
        getWorldLocation().getX(),
        getWorldLocation().getY(),
        (int) getDimension().getWidth(),
        (int) getDimension().getHeight());
    objectLayer = layer;
  }


  public void unInitializeGameObject() {
    Perceptionallity.getGame().getGameManager().unregisterGameObject(this);
    if (this instanceof GameNPC)
      Perceptionallity.getGame().getGameManager().unregisterNPC((GameNPC) this);
    Perceptionallity.getGame().getGamePanel().remove(getComponent());
  }

  public int distanceTo(WorldLocation worldLocation) {
    return (int)
        Math.hypot(
            worldLocation.getX() - getWorldLocation().toCenterLocation(getDimension()).getX(),
            worldLocation.getY() - getWorldLocation().toCenterLocation(getDimension()).getY());
  }

  public ResourceManager getResourceManager() {
    return getGame().getResourceManager();
  }

  public Game getGame() {
    return Perceptionallity.getGame();
  }

  public GameManager getGameManager() {
    return getGame().getGameManager();
  }
}
