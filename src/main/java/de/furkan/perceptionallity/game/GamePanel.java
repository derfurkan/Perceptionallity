package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import javax.swing.*;

public class GamePanel extends JLayeredPane {




  @Override
  public void paint(Graphics g) {
    super.paint(g);

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
