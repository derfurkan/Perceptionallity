package de.furkan.perceptionallity.game.entity.player;

import static java.awt.event.KeyEvent.*;

import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.game.GameKeyListener;
import de.furkan.perceptionallity.game.WorldLocation;
import de.furkan.perceptionallity.game.entity.GameEntity;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePlayer extends GameEntity {

  public GamePlayer(WorldLocation worldLocation) {
    super(new Dimension(90, 80), worldLocation, 100, 70, 4);
    playAnimation(getResourceManager().getResource("player_idle_down_animation", Animation.class));
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
                  ANIMATION_KEYS newKey = null;

                  switch (integer) {
                    case VK_W -> {
                      getCurrentVelocity().subtract(0, getMoveSpeed());
                      newKey = ANIMATION_KEYS.WALK_UP;
                    }
                    case VK_A -> {
                      getCurrentVelocity().subtract(getMoveSpeed(), 0);
                      newKey = ANIMATION_KEYS.WALK_LEFT;
                    }
                    case VK_D -> {
                      getCurrentVelocity().add(getMoveSpeed(), 0);
                      newKey = ANIMATION_KEYS.WALK_RIGHT;
                    }
                    case VK_S -> {
                      getCurrentVelocity().add(0, getMoveSpeed());
                      newKey = ANIMATION_KEYS.WALK_DOWN;
                    }
                  }
                  if (newKey == null) {
                    throw new RuntimeException(
                        "This exception should never ever come up. If it did came up there is something horrible going on.");
                  }

                  playAnimation(
                      getResourceManager().getResource(newKey.animationKey, Animation.class));
                }
              }

              @Override
              public void keyReleased(KeyEvent keyEvent) {
                // TODO: Fix that only valid pressed keys will trigger keyReleased
                getCurrentVelocity().set(0, 0);
                playAnimation(
                    getResourceManager().getResource("player_idle_up_animation", Animation.class));
              }
            },
            VK_W,
            VK_A,
            VK_S,
            VK_D);
  }

  enum ANIMATION_KEYS {
    WALK_UP("player_walk_up_animation"),
    WALK_DOWN("player_walk_down_animation"),
    WALK_LEFT("player_walk_left_animation"),
    WALK_RIGHT("player_walk_right_animation");

    final String animationKey;

    ANIMATION_KEYS(String animationKey) {
      this.animationKey = animationKey;
    }
  }
}
