package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.util.font.GameFont;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

public class MenuLabel extends MenuComponent {

  private final JLabel rawComponent;

  public MenuLabel(int x, int y, String text, float size, Color color) {
    super(x, y, new Dimension());
    rawComponent = new JLabel(text);
    rawComponent.setForeground(color);
    rawComponent.setFont(
        getResourceManager().getResource("menu_font", GameFont.class).getFont().deriveFont(size));

    // Calculating dimension by font metrics
    recalculateDimension();
  }

  public void setText(String newText) {
    rawComponent.setText(newText);
  }


  public void recalculateDimension() {
    Rectangle2D rectangle2D =
            rawComponent
                    .getFontMetrics(rawComponent.getFont())
                    .getStringBounds(rawComponent.getText(), rawComponent.getGraphics());
    Dimension dimension =
            new Dimension(
                    Math.toIntExact(Math.round(rectangle2D.getWidth())),
                    Math.toIntExact(Math.round(rectangle2D.getHeight())));
    setDimension(dimension);
  }

  @Override
  public JComponent getJComponent() {
    return rawComponent;
  }
}
