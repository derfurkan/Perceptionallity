package de.furkan.perceptionallity.game;

import java.awt.event.KeyEvent;

public interface GameKeyListener {

    void keyTyped(KeyEvent keyEvent);

    void whileKeyPressed();

    void keyReleased(KeyEvent keyEvent);
}