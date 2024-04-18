package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.resources.ResourceManager;
import lombok.Getter;

import java.awt.*;

@Getter
public abstract class MenuComponent {

  private final int x, y;
  private final Dimension dimension;

  public MenuComponent(int x, int y, Dimension dimension) {
    this.x = x;
    this.y = y;
    this.dimension = dimension;
  }

  public ResourceManager getResourceManager() {
    return Perceptionallity.getResourceManager();
  }
}
