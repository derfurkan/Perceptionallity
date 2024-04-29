package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.game.entity.player.GamePlayer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
import lombok.Getter;

@Getter
public class GameManager extends Manager {

  private final Camera camera = new Camera();

  private final HashMap<Component, GameObject> gameObjects = new HashMap<>();
  private final HashMap<Integer, GameKeyListener> keyListeners = new HashMap<>();
  private final List<LoopAction> loopCalls = new ArrayList<>();
  private final HashMap<GameObject, Animation> playingAnimations = new HashMap<>();
  private final Timer gameTimer;

  // Track currently pressed keys
  private final Set<Integer> pressedKeys = new HashSet<>();

  private final int GAME_UPDATE_MS = 30;
  private int updatesPassed = 0;  // Time accumulated from timer ticks

  public GameManager() {
    gameTimer = new Timer(GAME_UPDATE_MS, e -> loopCalls.forEach(LoopAction::onLoop));

    // Each frame render loop
    registerLoopAction(
        () -> {
          updatesPassed += 1;
          keyListeners.forEach(
              (integer, listener) -> {
                if (pressedKeys.contains(integer)) {
                  listener.whileKeyPressed(integer);
                }
              });

          gameObjects.forEach(
              (component, gameObject) ->
                  gameObject.getWorldLocation().applyVelocity(gameObject.getCurrentVelocity()));

          playingAnimations.forEach(
              (gameObject, animation) -> {
                if (updatesPassed % ((1000 / GAME_UPDATE_MS) / animation.getFramesPerSecond())
                    == 0) {
                  animation.getCurrentFrame().resize(gameObject.getRectangle().getSize());
                  gameObject.getComponent().setIcon(animation.getCurrentFrame().getRawImageIcon());
                  animation.nextFrame();

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
    GamePlayer gamePlayer1 = new GamePlayer(new WorldLocation(50, 50));

   // gamePlayer1.buildGameObject();

   // gamePlayer.registerKeyListener();
    gamePlayer.buildGameObject();

    // camera.centerOnObject(gamePlayer);

  }

  public void registerLoopAction(LoopAction loopAction) {
    loopCalls.add(loopAction);
  }

  public void registerKeyListener(GameKeyListener gameKeyListener, Integer... keyEvents) {
      getLogger().info("Registered new KeyListener "+ Arrays.toString(keyEvents));
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
    return gameObjects.containsKey(component);
  }

  public void registerGameObject(GameObject gameObject) {
      getLogger().info("Registered new GameObject ("+gameObject.getClass().getSimpleName()+")");
    gameObjects.put(gameObject.getComponent(), gameObject);
  }

  public void unregisterGameObject(GameObject gameObject) {
      getLogger().info("Unregistered GameObject ("+gameObject.getClass().getSimpleName()+")");
    gameObjects.remove(gameObject.getComponent());
  }
}
