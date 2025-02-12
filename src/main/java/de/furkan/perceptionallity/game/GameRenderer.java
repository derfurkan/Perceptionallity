package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

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


        // Debug Lines Pass
        if (!Perceptionallity.getGame().isDebug() || !Perceptionallity.getGame().isShowDebugLines())
            return;

        for (Component component : getComponentsInLayer(0)) {

            if (!getCamera().isComponentInView(component))
                continue;

            Rectangle bounds = component.getBounds();
            g.setColor(Color.RED);
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

            if (Perceptionallity.getGame().getGameManager().isGameComponent(component)) {

                GameObject gameObject =
                        Perceptionallity.getGame().getGameManager().getGameObjectByComponent(component);

                // Change this:
                if (gameObject.getCollisionBoundaries() == null) {
                    continue;
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
