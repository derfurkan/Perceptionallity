package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class MenuComponent {

  private int x, y;

  private Dimension dimension;

  public MenuComponent(int x, int y, Dimension dimension) {
    this.x = x;
    this.y = y;
    this.dimension = dimension;
  }

  public void setAbove(MenuComponent menuComponent) {
    setY(menuComponent.getY() - dimension.height);
  }

  public void setBelow(MenuComponent menuComponent) {
    setY(menuComponent.getY() + menuComponent.getDimension().height);
  }


  public void setXY(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void setAlpha(float alpha) {
    Color color = getJComponent().getForeground();
    getJComponent()
        .setForeground(
            new Color(
                (float) color.getRed() / 255,
                (float) color.getGreen() / 255,
                (float) color.getBlue() / 255,
                alpha));
  }

  public void buildComponent() {
    getJComponent().setBounds(x, y, dimension.width, dimension.height);
  }

  public void addMouseListener(MouseListener mouseListener) {
    getJComponent().addMouseListener(mouseListener);
  }

  public void addMouseMotionListener(MouseMotionListener mouseListener) {
    getJComponent().addMouseMotionListener(mouseListener);
  }

  public void addKeyListener(KeyListener keyListener) {
    getJComponent().addKeyListener(keyListener);
  }

  public abstract JComponent getJComponent();

  public ResourceManager getResourceManager() {
    return Perceptionallity.getGame().getResourceManager();
  }
}
