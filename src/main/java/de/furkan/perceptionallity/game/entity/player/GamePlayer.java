package de.furkan.perceptionallity.game.entity.player;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.game.GameKeyListener;
import de.furkan.perceptionallity.game.WorldLocation;
import de.furkan.perceptionallity.game.entity.GameEntity;
import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePlayer extends GameEntity {

  public GamePlayer(WorldLocation worldLocation, String textureName) {
    super(
        new Rectangle(worldLocation.getX(), worldLocation.getY(), 50, 80),
        worldLocation,
        Perceptionallity.getGame().getResourceManager().getResource(textureName, Sprite.class),
        100,
        70);

    setDefaultAnimation(getResourceManager().getResource("player_animation", Animation.class));
    getDefaultAnimation().start(this);
  }

  /**
   * Registers a GameKeyListener with the GameManager class. This listener listens for key events
   * and performs specific actions based on the key pressed.
   */
  public void registerKeyListener() {
    getGameManager()
        .registerKeyListener(
            new GameKeyListener() {
              @Override
              public void keyTyped(KeyEvent keyEvent) {
                // No action needed for keyTyped event
              }

              @Override
              public void whileKeyPressed(int integer) {
                if (getCurrentVelocity().isZero()) {
                  switch (integer) {
                    case KeyEvent.VK_W:
                      getCurrentVelocity().subtract(0, 3);
                      break;
                    case KeyEvent.VK_A:
                      getCurrentVelocity().subtract(3, 0);
                      break;
                    case KeyEvent.VK_D:
                      getCurrentVelocity().add(3, 0);
                      break;
                    case KeyEvent.VK_S:
                      getCurrentVelocity().add(0, 3);
                      break;
                  }
                }
              }

              @Override
              public void keyReleased(KeyEvent keyEvent) {
                getCurrentVelocity().set(0, 0);
              }
            },
            KeyEvent.VK_W,
            KeyEvent.VK_A,
            KeyEvent.VK_S,
            KeyEvent.VK_D);
  }
}
