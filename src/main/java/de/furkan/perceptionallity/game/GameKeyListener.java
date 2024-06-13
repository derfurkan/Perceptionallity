package de.furkan.perceptionallity.game;

import java.awt.event.KeyEvent;

public interface GameKeyListener {

  void whileKeyPressed(int keyEvent) throws Exception;

  void keyReleased(KeyEvent keyEvent) throws Exception;
}
