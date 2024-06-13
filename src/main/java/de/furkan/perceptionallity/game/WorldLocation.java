package de.furkan.perceptionallity.game;

import java.awt.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorldLocation {

  private int x, y;

  public WorldLocation() {
    this.x = 0;
    this.y = 0;
  }

  public WorldLocation(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int[] getXY() {
    return new int[] {x, y};
  }

  public void setXY(int x, int y) {
    setX(x);
    setY(y);
  }

  public WorldLocation toCenterLocation(Dimension dimension) {
    return new WorldLocation(x + dimension.width / 2, y + dimension.height / 2);
  }

  public void set(WorldLocation worldLocation) {
    this.x = worldLocation.getX();
    this.y = worldLocation.getY();
  }

  public void applyVelocity(GameVelocity gameVelocity) {
    this.x += gameVelocity.getX();
    this.y += gameVelocity.getY();
  }
}
