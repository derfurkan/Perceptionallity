package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Perceptionallity;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JLayeredPane {


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(Perceptionallity.DEBUG_MODE) {
            // Iterate through all components
            for (Component comp : getComponents()) {
                // Get the bounds of the component
                Rectangle bounds = comp.getBounds();
                // Convert the bounds to the coordinate space of this component
                SwingUtilities.convertRectangle(comp.getParent(), bounds, this);
                // Draw a rectangle around the component's bounds
                g.setColor(Color.RED);
                g.drawRect(bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2);
            }
        }
    }
}
