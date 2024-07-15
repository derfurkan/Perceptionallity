package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import javax.swing.*;

public class GameRenderer extends JLayeredPane {

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
        var gameObject =
            Perceptionallity.getGame().getGameManager().getGameObjectByComponent(component);
        getCamera().finishGameObject(gameObject, getCamera().calculateObjectPosition(gameObject));
      }
    }

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

    // Debug Lines Pass
    if (!Perceptionallity.getGame().isDebug() || !Perceptionallity.getGame().isShowDebugLines())
      return;

    for (int i = 0; i < 5; i++) {

      for (Component component : getComponentsInLayer(i)) {

        Rectangle bounds = component.getBounds();
        SwingUtilities.convertRectangle(component.getParent(), bounds, this);
        g.setColor(Color.RED);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        if (Perceptionallity.getGame().getGameManager().isGameComponent(component)) {

          GameObject gameObject =
              Perceptionallity.getGame().getGameManager().getGameObjectByComponent(component);

          // Change this:
          if (gameObject.getCollisionBoundaries() == null) {
            return;
          }

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

  //  public void passToCollisionCheck(
  //      GameObject gameObject, CompletableFuture<Boolean> completableFuture) {
  //    collisionCheck.put(gameObject, completableFuture);
  //  }

  public Camera getCamera() {
    return Perceptionallity.getGame().getGameManager().getCamera();
  }

  public GameManager getGameManager() {
    return Perceptionallity.getGame().getGameManager();
  }
}
