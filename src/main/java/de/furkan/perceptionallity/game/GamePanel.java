package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
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
    for (Component component : getComponents()) {
      if (Perceptionallity.getGame().getGameManager().isGameComponent(component)) {
        GameObject gameObject =
            Perceptionallity.getGame().getGameManager().getGameObjectByComponent(component);
        int[] newPos = getCamera().calculateObjectPosition(gameObject);

        getCamera().finishGameObject(gameObject, newPos);
      }
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

    int layerCount = highestLayer() == 0 ? 1 : highestLayer();

    for (int i = 0; i < layerCount; i++) {

      for (int j = 0; j < getComponentCountInLayer(i); j++) {
        Component comp = getComponentsInLayer(i)[j];

        Rectangle bounds = comp.getBounds();
        SwingUtilities.convertRectangle(comp.getParent(), bounds, this);
        g.setColor(Color.RED);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        if (Perceptionallity.getGame().getGameManager().isGameComponent(comp)) {

          GameObject gameObject =
              Perceptionallity.getGame().getGameManager().getGameObjectByComponent(comp);
          if (gameObject.getCollisionBoundaries() != null) {

            bounds =
                new Rectangle(
                    gameObject.getComponent().getX()
                        + (gameObject.getComponent().getWidth() / 2)
                        - gameObject.getCollisionBoundaries().width / 2,
                    (gameObject.getComponent().getY()
                            + (gameObject.getComponent().getHeight() / 2)
                            - (gameObject.getCollisionBoundaries().height / 2))
                        - 5,
                    gameObject.getCollisionBoundaries().width,
                    gameObject.getCollisionBoundaries().height);

            SwingUtilities.convertRectangle(comp.getParent(), bounds, this);
            g.setColor(Color.GREEN);
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

            // To my future self: Remove code nesting at all costs later pls..

            // Response to my past self: Fuck this code nesting. Better kill yourself
          }
        }
      }
    }
  }

  public void passToCollisionCheck(
      GameObject gameObject, CompletableFuture<Boolean> completableFuture) {
    collisionCheck.put(gameObject, completableFuture);
  }

  public Camera getCamera() {
    return Perceptionallity.getGame().getGameManager().getCamera();
  }
}
