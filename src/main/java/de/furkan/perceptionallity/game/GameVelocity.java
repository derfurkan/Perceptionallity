package de.furkan.perceptionallity.game;

import lombok.Getter;

@Getter
public class GameVelocity {

  private int x;
  private int y;

  public GameVelocity(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public boolean isZero() {
    return this.x == 0 && this.y == 0;
  }

  public void reverse() {
    this.x = -this.x;
    this.y = -this.y;
  }

  public void set(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void subtract(int x, int y) {
    this.x -= x;
    this.y -= y;
  }

  public void applyFactor(int factor) {
    if (this.x != 0) {
      this.x += (this.x < 0) ? -factor : factor;
    }
    if (this.y != 0) {
      this.y += (this.y < 0) ? -factor : factor;
    }
  }

  public void add(int x, int y) {
    this.x += x;
    this.y += y;
  }
}
