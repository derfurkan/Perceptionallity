package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.entity.player.GamePlayer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class GamePanel extends JLayeredPane {

  private final HashMap<GameObject, CompletableFuture<Boolean>> collisionCheck = new HashMap<>();

  /**
   * Overrides the paintComponent method to handle custom rendering of game components. This method
   * first flushes the camera's calculation cache, then recalculates and sets new bounds for each
   * game component based on the camera's calculations. If debug mode is enabled and showing debug
   * lines is set, it also draws red rectangles around each component to visualize their bounds.
   *
   * @param g The Graphics object to protect.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    getCamera().flushCalculation();

    ArrayList<GameObject> validObjects = new ArrayList<>();
    ExecutorService executorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    List<Component> components = Arrays.asList(getComponents());
    int chunkSize = components.size() > 100 ? components.size() / 100 : 1;
    List<List<Component>> chunks = splitArrayList(components, chunkSize);

    for (List<Component> chunk : chunks) {
      executorService.submit(
          () -> {
            for (Component component : chunk) {
              if (Perceptionallity.getGame().getGameManager().isGameComponent(component)) {
                GameObject gameObject =
                    Perceptionallity.getGame().getGameManager().getGameObjectByComponent(component);

                if (gameObject instanceof GamePlayer
                    || gameObject.distanceTo(
                            Perceptionallity.getGame()
                                .getGameManager()
                                .getCurrentPlayer()
                                .getWorldLocation())
                        < Perceptionallity.getGame().getGameManager().getDISTANCE_UNTIL_DISPOSE()) {

                  synchronized (validObjects) {
                    validObjects.add(gameObject);
                  }
                } else {
                    collisionCheck.remove(gameObject);
                }
              }
            }
          });
    }

    executorService.shutdown();
    try {
      executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    for (GameObject component : validObjects) {
      int[] newPos = getCamera().calculateObjectPosition(component);
      getCamera().finishGameObject(component, newPos);
    }

    // TODO: Create collision handler

    // Collision check start
    new HashMap<>(collisionCheck)
        .forEach(
            (gameObject, booleanCompletableFuture) -> {
              if (!getCamera().getCalculatedGameObjects().containsKey(gameObject)
                  || gameObject.getCollisionBoundaries() == null) {
                return;
              }

              int[] calPos = getCamera().getCalculatedGameObjects().get(gameObject);
              Rectangle futureRect =
                  new Rectangle(
                      calPos[0],
                      calPos[1],
                      gameObject.getCollisionBoundaries().width,
                      gameObject.getCollisionBoundaries().height);

              new HashMap<>(getCamera().getCalculatedGameObjects())
                  .forEach(
                      (gameObject1, ints) -> {
                        if (gameObject == gameObject1
                            || gameObject1.getCollisionBoundaries() == null) return;

                        if (futureRect.intersects(
                            new Rectangle(
                                ints[0],
                                ints[1],
                                gameObject1.getCollisionBoundaries().width,
                                gameObject1.getCollisionBoundaries().height))) {

                          booleanCompletableFuture.complete(true);
                        }
                      });

              if (!booleanCompletableFuture.isDone()) {
                booleanCompletableFuture.complete(false);
              }

              collisionCheck.remove(gameObject);
            });

    // Collision check end

    // Render final game objects
    getCamera()
        .getCalculatedGameObjects()
        .forEach(
            (gameObject, newLoc) ->
                gameObject
                    .getComponent()
                    .setBounds(
                        new Rectangle(
                            newLoc[0],
                            newLoc[1],
                            (int) gameObject.getDimension().getWidth(),
                            (int) gameObject.getDimension().getHeight())));

    if (!Perceptionallity.getGame().isDebug() || !Perceptionallity.getGame().isShowDebugLines())
      return;

    // Debug Lines Pass
    for (int i = 0; i < 5; i++) {

      for (Component component : getComponentsInLayer(i)) {

        Rectangle bounds = component.getBounds();
        SwingUtilities.convertRectangle(component.getParent(), bounds, this);
        g.setColor(Color.RED);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        if (Perceptionallity.getGame().getGameManager().isGameComponent(component)) {

          GameObject gameObject =
              Perceptionallity.getGame().getGameManager().getGameObjectByComponent(component);
          if (gameObject.getCollisionBoundaries() != null) {

            bounds =
                new Rectangle(
                    gameObject.getComponent().getX(),
                    gameObject.getComponent().getY(),
                    gameObject.getCollisionBoundaries().width,
                    gameObject.getCollisionBoundaries().height);

            SwingUtilities.convertRectangle(component.getParent(), bounds, this);
            g.setColor(Color.GREEN);
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

            // To my future self: Remove code nesting at all costs later pls..

            // Response to my past self: Fuck this code nesting. Better kill yourself
          }
        }
      }
    }
  }

  private <T> List<List<T>> splitArrayList(List<T> list, int chunkSize) {
    List<List<T>> chunks = new ArrayList<>();
    int listSize = list.size();

    for (int i = 0; i < listSize; i += chunkSize) {
      int end = Math.min(listSize, i + chunkSize);
      chunks.add(new ArrayList<>(list.subList(i, end)));
    }

    return chunks;
  }

  public void passToCollisionCheck(
      GameObject gameObject, CompletableFuture<Boolean> completableFuture) {
    collisionCheck.put(gameObject, completableFuture);
  }

  public Camera getCamera() {
    return Perceptionallity.getGame().getGameManager().getCamera();
  }
}
