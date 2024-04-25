package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Manager;

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

    registerLoopAction(
            () -> keyListeners.forEach((integer,listener) -> {
              if(pressedKeys.contains(integer)) {
                listener.whileKeyPressed();
              }
            }));

    registerKeyListener(
        new int[] {KeyEvent.VK_W, KeyEvent.VK_S},
        new GameKeyListener() {
          @Override
          public void keyTyped(KeyEvent keyEvent) {}

          @Override
          public void whileKeyPressed() {
            // FUCK YEAH THIS IS WORKING!
          }

          @Override
          public void keyReleased(KeyEvent keyEvent) {}
        });
  }

  @Override
  public void initialize() {

    getGame().getGameFrame().addKeyListener(new java.awt.event.KeyListener() {
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

    startGameLoop();
  }

  public void registerLoopAction(LoopAction loopAction) {
    loopCalls.add(loopAction);
  }

  public void registerKeyListener(int[] keyCodes, GameKeyListener gameKeyListener) {
    for (int keyCode : keyCodes) {
      keyListeners.put(keyCode,gameKeyListener);
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
