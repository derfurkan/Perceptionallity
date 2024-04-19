package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class MenuComponent {

  private int x, y;

  private Dimension dimension;

  public MenuComponent(int x, int y, Dimension dimension) {
    this.x = x;
    this.y = y;
    this.dimension = dimension;

  }

  public void setXY(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public ResourceManager getResourceManager() {
    return Perceptionallity.getResourceManager();
  }
}
