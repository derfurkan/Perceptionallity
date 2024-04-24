package de.furkan.perceptionallity.game.world;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.WorldLocation;
import java.awt.*;

import de.furkan.perceptionallity.resources.ResourceManager;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class GameObject {

  private final WorldLocation worldLocation;
  private final Rectangle rectangle;

  @Setter
  private Component component;

  public GameObject(Rectangle rectangle, WorldLocation worldLocation) {
    this.worldLocation = worldLocation;
    this.rectangle = rectangle;
  }

  public abstract void buildGameObject();


  public ResourceManager getResourceManager() {
    return Perceptionallity.getGame().getResourceManager();
  }

}
