package de.furkan.perceptionallity.game;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GameKeyEvent {

  private final GameKeyListener keyListener;
  private final Integer[] keyRegister;
  private final List<Integer> pressedKeys = new ArrayList<>();

  public GameKeyEvent(GameKeyListener keyListener, Integer... keyRegister) {
    this.keyListener = keyListener;
    this.keyRegister = keyRegister;
  }
}
