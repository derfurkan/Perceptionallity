package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.util.font.GameFont;
import java.awt.*;
import javax.swing.*;

public class MenuLabel extends MenuComponent {

  private final JLabel rawComponent;

  public MenuLabel(int x, int y, Dimension dimension, String text, float size, Color color) {
    super(x, y, dimension);
    rawComponent = new JLabel(text);
    rawComponent.setForeground(color);
    rawComponent.setBounds(getX(), getY(), getDimension().width, getDimension().height);
    rawComponent.setFont(
        getResourceManager().getResource("menu_font", GameFont.class).getFont().deriveFont(size));
  }

  public JLabel getLabel() {
    return rawComponent;
  }
}
