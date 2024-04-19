package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import javax.swing.*;

public class MenuPanel extends JLayeredPane {

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    if (Perceptionallity.DEBUG_MODE) {
      for (Component comp : getComponents()) {
        Rectangle bounds = comp.getBounds();
        SwingUtilities.convertRectangle(comp.getParent(), bounds, this);
        g.setColor(Color.RED);
        g.drawRect(bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2);
      }
    }
  }
}
