package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import javax.swing.*;

public class GamePanel extends JLayeredPane {

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    // Calculates the position of the objects on the camera and renders them based on the result of the camera calculation.
    Perceptionallity.getGame().getGameManager().getCamera().flushCalculation();
    for (Component component : getComponents()) {
      if (Perceptionallity.getGame().getGameManager().isGameComponent(component)) {
        int[] newPos =
            Perceptionallity.getGame()
                .getGameManager()
                .getCamera()
                .calculateObjectPosition(
                    Perceptionallity.getGame()
                        .getGameManager()
                        .getRegisteredGameObjects()
                        .get(component));
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
