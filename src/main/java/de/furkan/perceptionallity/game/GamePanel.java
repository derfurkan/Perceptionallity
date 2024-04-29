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

    // Collision check start
    new HashMap<>(collisionCheck)
        .forEach(
            (gameObject, booleanCompletableFuture) -> {
              if (!getCamera().getCalculatedGameObjects().containsKey(gameObject)) {
                return;
              }
              int[] calPos = getCamera().getCalculatedGameObjects().get(gameObject);
              Rectangle futureRect =
                  new Rectangle(
                      calPos[0],
                      calPos[1],
                      gameObject.getDimension().width,
                      gameObject.getDimension().height);

              new HashMap<>(getCamera().getCalculatedGameObjects())
                  .forEach(
                      (gameObject1, ints) -> {
                        if (gameObject == gameObject1) return;

                        if (futureRect.intersects(
                            new Rectangle(
                                ints[0],
                                ints[1],
                                gameObject1.getDimension().width,
                                gameObject1.getDimension().height))) {

                          booleanCompletableFuture.complete(true);
                        }
                      });

              if (!booleanCompletableFuture.isDone()) {
                booleanCompletableFuture.complete(false);
              } else if (getCamera().getCenteredObject() != null
                  && getCamera().getCenteredObject() == gameObject) {
                getCamera().flushCalculation();
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

    if (Perceptionallity.getGame().isDebug() && Perceptionallity.getGame().isShowDebugLines()) {
      for (Component comp : getComponents()) {
        Rectangle bounds = comp.getBounds();
        SwingUtilities.convertRectangle(comp.getParent(), bounds, this);
        g.setColor(Color.RED);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
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
