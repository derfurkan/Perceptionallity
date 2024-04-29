package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Game;
import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
import java.util.Optional;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class GameObject {

  private final WorldLocation worldLocation;
  private final Dimension dimension;
  private final boolean isCollidable;
  private final GameVelocity currentVelocity;
  private Optional<Animation> currentPlayingAnimation = Optional.empty();
  @Setter private boolean isAnimationFresh = true;
  @Setter private JLabel component;

  public GameObject(Dimension dimension, WorldLocation worldLocation, boolean isCollidable) {
    this.worldLocation = worldLocation;
    this.isCollidable = isCollidable;
    this.currentVelocity = new GameVelocity(0, 0);
    this.dimension = dimension;
  }

  public void playAnimation(Animation animation) {
    currentPlayingAnimation = Optional.of(animation);
    setAnimationFresh(true);
  }

  public Rectangle buildRectangle() {
    return new Rectangle(
        worldLocation.getX(), worldLocation.getY(), dimension.width, dimension.height);
  }

  public abstract void buildGameObject();

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
