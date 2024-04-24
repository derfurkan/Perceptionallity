package de.furkan.perceptionallity.game;

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
}
