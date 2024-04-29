package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.game.entity.player.GamePlayer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.swing.Timer;
import lombok.Getter;

@Getter
public class GameManager extends Manager {

  private final Camera camera = new Camera();

  private final List<GameObject> gameObjects = new ArrayList<>();
  private final HashMap<Integer, GameKeyListener> keyListeners = new HashMap<>();
  private final List<LoopAction> loopCalls = new ArrayList<>();
  private final Timer gameTimer;

  private final Set<Integer> pressedKeys = new HashSet<>();

  private final int GAME_UPDATE_MS = 30;
  private long updatesPassed = 0;

  public GameManager() {
    gameTimer = new Timer(GAME_UPDATE_MS, e -> loopCalls.forEach(LoopAction::onLoop));

    // Each frame render loop
    registerLoopAction(
        () -> {
          // Just on case if a player has no real life:
          if (updatesPassed == Long.MAX_VALUE) updatesPassed = 0;
          updatesPassed += 1;

          keyListeners.forEach(
              (integer, listener) -> {
                if (pressedKeys.contains(integer)) {
                  listener.whileKeyPressed(integer);
                }
              });

          // GameObject Pass
          gameObjects.forEach(
              (gameObject) -> {
                int[] beforeLocation = gameObject.getWorldLocation().getXY();

                gameObject.getWorldLocation().applyVelocity(gameObject.getCurrentVelocity());
                CompletableFuture<Boolean> collisionFuture = new CompletableFuture<>();

                getGame().getGamePanel().passToCollisionCheck(gameObject, collisionFuture);

                collisionFuture.whenComplete(
                    (isColliding, throwable) -> {
                      if (isColliding) {
                        gameObject.getWorldLocation().setXY(beforeLocation[0], beforeLocation[1]);
                        camera.getCalculatedGameObjects().remove(gameObject);
                      }
                    });

                if (gameObject.getCurrentPlayingAnimation().isPresent()) {
                  if ((updatesPassed
                              % ((1000 / GAME_UPDATE_MS)
                                  / gameObject
                                      .getCurrentPlayingAnimation()
                                      .get()
                                      .getFramesPerSecond())
                          == 0)
                      || gameObject.isAnimationFresh()) {
                    if (gameObject.isAnimationFresh()) gameObject.setAnimationFresh(false);
                    gameObject
                        .getCurrentPlayingAnimation()
                        .get()
                        .getCurrentFrame()
                        .resize(gameObject.getDimension());
                    gameObject
                        .getComponent()
                        .setIcon(
                            gameObject
                                .getCurrentPlayingAnimation()
                                .get()
                                .getCurrentFrame()
                                .getRawImageIcon());
                    gameObject.getCurrentPlayingAnimation().get().nextFrame();
                  }
                }
              });

          getGame().getGamePanel().repaint();
          getGame().getGamePanel().revalidate();
        });
  }

  @Override
  public void initialize() {
    startGameLoop();
    getGame()
        .getGameFrame()
        .addKeyListener(
            new java.awt.event.KeyListener() {
              @Override
              public void keyTyped(KeyEvent e) {
                triggerKeyListener(e);
              }

              @Override
              public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
              }

              @Override
              public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                triggerKeyListener(e);
              }

              private void triggerKeyListener(KeyEvent e) {
                GameKeyListener listener = keyListeners.get(e.getKeyCode());
                if (listener != null) {
                  if (e.getID() == KeyEvent.KEY_TYPED) {
                    listener.keyTyped(e);
                  } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    listener.keyReleased(e);
                  }
                }
              }
            });

    GamePlayer gamePlayer = new GamePlayer(new WorldLocation(20, 20));
    GamePlayer gamePlayer1 = new GamePlayer(new WorldLocation(170, 170));

    gamePlayer1.buildGameObject();

    gamePlayer.registerKeyListener();
    gamePlayer.buildGameObject();

    camera.centerOnObject(gamePlayer);
  }

  public void registerLoopAction(LoopAction loopAction) {
    loopCalls.add(loopAction);
  }

  public void registerKeyListener(GameKeyListener gameKeyListener, Integer... keyEvents) {
    getLogger().info("Registered new KeyListener " + Arrays.toString(keyEvents));
    for (Integer integer : keyEvents) {
      keyListeners.put(integer, gameKeyListener);
    }
  }

  private void startGameLoop() {
    getLogger().info("Started game loop");
    gameTimer.start();
  }

  private void stopGameLoop() {
    getLogger().info("Stopped game loop");
    gameTimer.stop();
  }

  public boolean isGameComponent(Component component) {
    return gameObjects.stream().anyMatch(gameObject -> gameObject.getComponent() == component);
  }

  public GameObject getGameObjectByComponent(Component component) {
    return gameObjects.stream()
        .filter(gameObject -> gameObject.getComponent() == component)
        .findFirst()
        .get();
  }

  public void registerGameObject(GameObject gameObject) {
    getLogger().info("Registered new GameObject (" + gameObject.getClass().getSimpleName() + ")");
    gameObjects.add(gameObject);
  }

  public void unregisterGameObject(GameObject gameObject) {
    getLogger().info("Unregistered GameObject (" + gameObject.getClass().getSimpleName() + ")");
    gameObjects.remove(gameObject.getComponent());
  }
}
