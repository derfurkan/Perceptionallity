package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.util.font.GameFont;
import java.awt.*;
import javax.swing.*;

public class MenuLabel extends MenuComponent {

  private final JLabel rawComponent;
  private final String text;
  private final int size;

  public MenuLabel(int x, int y, Dimension dimension, String text, int size) {
    super(x, y, dimension);
    this.text = text;
    this.size = size;

    rawComponent = new JLabel(text);
    rawComponent.setFont(getResourceManager().getResource("menu_font", GameFont.class).getFont());
  }

  public JLabel getLabel() {
    return rawComponent;
  }
}
