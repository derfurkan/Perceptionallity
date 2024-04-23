package de.furkan.perceptionallity;

import lombok.Getter;

public class Perceptionallity {

  @Getter private static Game game;

  public static void main(String[] args) {
    game = new Game();
    game.start();
  }
}
