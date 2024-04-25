package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.game.entity.player.GamePlayer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import lombok.Getter;

@Getter
public class GameManager extends Manager {

  private final Camera camera = new Camera();

  private final HashMap<Component, GameObject> gameObjects = new HashMap<>();
  private final HashMap<Integer, GameKeyListener> keyListeners = new HashMap<>();
  private final java.util.List<LoopAction> loopCalls = new ArrayList<>();
  private final Timer gameTimer;

  // Track currently pressed keys
  private final Set<Integer> pressedKeys = new HashSet<>();

  public GameManager() {
    gameTimer = new Timer(30, e -> loopCalls.forEach(LoopAction::onLoop));

    registerLoopAction(() -> {

        keyListeners.forEach(
                (integer, listener) -> {
                    if (pressedKeys.contains(integer)) {
                        listener.whileKeyPressed(integer);
                    }
                });

        gameObjects.forEach((component,gameObject) -> {
            gameObject.getWorldLocation().applyVelocity(gameObject.getCurrentVelocity());
        });

        getGame().getGamePanel().repaint();
        getGame().getGamePanel().revalidate();
    });

  }

  @Override
  public void initialize() {

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
                  GameKeyListener listener = keyListeners.get(e.getKeyCode());
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

    GamePlayer gamePlayer = new GamePlayer(new WorldLocation(20, 20), "initial_player");
      GamePlayer gamePlayer1 = new GamePlayer(new WorldLocation(50, 50), "initial_player");

      gamePlayer1.buildGameObject();

    gamePlayer.registerKeyListener();
    gamePlayer.buildGameObject();

    camera.centerOnObject(gamePlayer);

    startGameLoop();
  }

  public void registerLoopAction(LoopAction loopAction) {
    loopCalls.add(loopAction);
  }

  public void registerKeyListener(GameKeyListener gameKeyListener, Integer... keyEvents) {
    for (Integer integer : keyEvents) {
      keyListeners.put(integer, gameKeyListener);
    }
  }

  private void startGameLoop() {
    gameTimer.start();
  }

  private void stopGameLoop() {
    gameTimer.stop();
  }

  public boolean isGameComponent(Component component) {
    return gameObjects.containsKey(component);
  }

  public void registerGameObject(GameObject gameObject) {
    gameObjects.put(gameObject.getComponent(), gameObject);
  }

  public void unregisterGameObject(GameObject gameObject) {
    gameObjects.remove(gameObject.getComponent());
  }
}
