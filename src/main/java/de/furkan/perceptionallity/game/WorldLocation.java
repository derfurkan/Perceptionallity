package de.furkan.perceptionallity.game;

import java.awt.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorldLocation {

  private int x, y;

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

  public void applyVelocity(GameVelocity gameVelocity) {
    this.x += gameVelocity.getX();
    this.y += gameVelocity.getY();
  }
}
