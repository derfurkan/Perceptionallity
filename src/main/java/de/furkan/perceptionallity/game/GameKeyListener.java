package de.furkan.perceptionallity.game;

import java.awt.event.KeyEvent;

public interface GameKeyListener {

  void whileKeyPressed(int keyEvent);

  void keyReleased(KeyEvent keyEvent);
}
