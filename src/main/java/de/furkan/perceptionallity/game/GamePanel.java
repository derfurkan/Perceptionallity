package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import javax.swing.*;

public class GamePanel extends JLayeredPane {

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

    Perceptionallity.getGame().getGameManager().getCamera().flushCalculation();
    for (Component component : getComponents()) {
      if (Perceptionallity.getGame().getGameManager().isGameComponent(component)) {
        int[] newPos =
            Perceptionallity.getGame()
                .getGameManager()
                .getCamera()
                .calculateObjectPosition(
                    Perceptionallity.getGame().getGameManager().getGameObjects().get(component));
        component.setBounds(
            new Rectangle(newPos[0], newPos[1], component.getWidth(), component.getHeight()));
      }
    }

    if (Perceptionallity.getGame().isDebug() && Perceptionallity.getGame().isShowDebugLines()) {
      for (Component comp : getComponents()) {
        Rectangle bounds = comp.getBounds();
        SwingUtilities.convertRectangle(comp.getParent(), bounds, this);
        g.setColor(Color.RED);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
      }
    }
  }
}
