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

    Rectangle2D rectangle2D =
        rawComponent
            .getFontMetrics(rawComponent.getFont())
            .getStringBounds(text, rawComponent.getGraphics());
    Dimension dimension =
        new Dimension(
            Math.toIntExact(Math.round(rectangle2D.getWidth() + 1)),
            Math.toIntExact(Math.round(rectangle2D.getHeight())));
    setDimension(dimension);
  }

  public void setAlpha(float alpha) {
    rawComponent.setForeground(
        new Color(
            (float) rawComponent.getForeground().getRed() / 255,
            (float) rawComponent.getForeground().getGreen() / 255,
            (float) rawComponent.getForeground().getBlue() / 255,
            alpha));
  }

  public void buildLabel() {
    rawComponent.setBounds(getX(), getY(), getDimension().width, getDimension().height);
  }

  public JLabel getLabel() {
    return rawComponent;
  }
}
