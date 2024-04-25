package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Game;
import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class GameObject {

  private final WorldLocation worldLocation;
  private final Rectangle rectangle;
  private final GameVelocity currentVelocity;
  @Setter private Component component;

  public GameObject(Rectangle rectangle, WorldLocation worldLocation) {
    this.worldLocation = worldLocation;
    this.currentVelocity = new GameVelocity(0,0);
    this.rectangle = rectangle;
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
