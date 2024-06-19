package de.furkan.perceptionallity.game.entity.player;

import static java.awt.event.KeyEvent.*;

import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.game.*;
import de.furkan.perceptionallity.game.entity.EntityAttributes;
import de.furkan.perceptionallity.game.entity.GameEntity;
import de.furkan.perceptionallity.game.entity.npc.GameNPC;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePlayer extends GameEntity {

  /**
   * Registers a GameKeyListener with the GameManager class. This listener listens for key events
   * and performs specific actions based on the key pressed.
   */
  GameKeyEvent gameKeyEvent;

  private DIRECTION lastDirection = DIRECTION.SOUTH;
  private int successWalk;

  public GamePlayer(WorldLocation worldLocation, boolean passToCollisionCheck) throws Exception {
    super(new Dimension(100, 110), worldLocation, passToCollisionCheck);
    playAnimation(getResourceManager().getResource("player_idle_down_animation", Animation.class));
    getGameManager()
        .registerLoopAction(
            new GameAction() {
              @Override
              public void onAction() {

                getGameManager()
                    .getGameNPCs()
                    .forEach(
                        gameNPC -> {
                          if (distanceTo(
                                  gameNPC
                                      .getWorldLocation()
                                      .toCenterLocation(gameNPC.getDimension()))
                              < 75) {
                            gameNPC.showInteractArrow();
                          } else {
                            gameNPC.hideInteractArrow();
                          }
                        });
              }
            });
  }

  public void registerKeyEvent() {

    getGameManager()
        .registerKeyEvent(
            gameKeyEvent =
                new GameKeyEvent(
                    new GameKeyListener() {

                      @Override
                      public void whileKeyPressed(int integer) throws Exception {

                        if (getGameManager().isGameState(GameState.IN_DIALOGUE)) {
                          handleDialogueKey(integer);
                          return;
                        }

                        if (getCurrentVelocity().isZero()
                            && getGameManager().isGameState(GameState.IN_GAME)) {
                          int movementSpeed = getAttribute(EntityAttributes.MOVEMENT_SPEED);
                          ANIMATION_KEYS newKey = null;

                          switch (integer) {
                            case VK_W -> {
                              getCurrentVelocity().set(0, -movementSpeed);
                              newKey = ANIMATION_KEYS.WALK_UP;
                              lastDirection = DIRECTION.NORTH;
                            }
                            case VK_A -> {
                              getCurrentVelocity().set(-movementSpeed, 0);
                              newKey = ANIMATION_KEYS.WALK_LEFT;
                              lastDirection = DIRECTION.WEST;
                            }
                            case VK_S -> {
                              getCurrentVelocity().set(0, movementSpeed);
                              newKey = ANIMATION_KEYS.WALK_DOWN;
                              lastDirection = DIRECTION.SOUTH;
                            }
                            case VK_D -> {
                              getCurrentVelocity().set(movementSpeed, 0);
                              newKey = ANIMATION_KEYS.WALK_RIGHT;
                              lastDirection = DIRECTION.OST;
                            }
                            case VK_ENTER -> {
                              for (GameNPC gameNPC : getGameManager().getGameNPCs()) {
                                if (gameNPC.isInteractionArrowShown()) {
                                  // CHECK NPC
                                  getGameManager().setGameState(GameState.IN_DIALOGUE);
                                  break;
                                }
                              }

                              return;
                            }
                          }

                          if (newKey == null) {
                            getGame()
                                .handleFatalException(
                                    new RuntimeException(
                                        "This exception should never ever come up. If it did came up there is something horrible going on."));
                          }
                          successWalk = integer;

                          playAnimation(
                              getResourceManager()
                                  .getResource(newKey.animationKey, Animation.class),
                              8);
                        }
                      }

                      @Override
                      public void keyReleased(KeyEvent keyEvent) throws Exception {
                        if (keyEvent.getKeyCode() == successWalk) {
                          getCurrentVelocity().set(0, 0);

                          ANIMATION_KEYS idleAnimation = null;

                          switch (lastDirection) {
                            case NORTH -> idleAnimation = ANIMATION_KEYS.IDLE_UP;
                            case OST -> idleAnimation = ANIMATION_KEYS.IDLE_RIGHT;
                            case SOUTH -> idleAnimation = ANIMATION_KEYS.IDLE_DOWN;
                            case WEST -> idleAnimation = ANIMATION_KEYS.IDLE_LEFT;
                          }

                          playAnimation(
                              getResourceManager()
                                  .getResource(idleAnimation.animationKey, Animation.class));
                        }
                      }
                    },
                    VK_W,
                    VK_A,
                    VK_S,
                    VK_D,
                    VK_ENTER,
                    VK_ESCAPE));
  }

  private void handleDialogueKey(int key) {}

  enum DIRECTION {
    NORTH,
    OST,
    SOUTH,
    WEST
  }

  enum ANIMATION_KEYS {
    WALK_UP("player_walk_up_animation"),
    WALK_DOWN("player_walk_down_animation"),
    WALK_LEFT("player_walk_left_animation"),
    WALK_RIGHT("player_walk_right_animation"),
    IDLE_UP("player_idle_up_animation"),
    IDLE_DOWN("player_idle_down_animation"),
    IDLE_LEFT("player_idle_left_animation"),
    IDLE_RIGHT("player_idle_right_animation");

    final String animationKey;

    ANIMATION_KEYS(String animationKey) {
      this.animationKey = animationKey;
    }
  }
}
