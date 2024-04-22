package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
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
    setY(menuComponent.getY() - menuComponent.getDimension().height);
  }

  public void setBelow(MenuComponent menuComponent) {
    setY(menuComponent.getY() + menuComponent.getDimension().height);
  }

  public void setAsideRight(MenuComponent menuComponent) {
    setX(menuComponent.getX() + menuComponent.getDimension().width);
  }

  public void setAsideLeft(MenuComponent menuComponent) {
    setX(menuComponent.getX() - menuComponent.getDimension().width);
  }

  public void setXY(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public float getAlpha() {
    Color color = getJComponent().getForeground();
    return (float) color.getAlpha() / 255;
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

  public boolean isSteadyComponent() {
    return Perceptionallity.getGame()
        .getMenuManager()
        .getCurrentMenu()
        .isSteadyComponent(getJComponent());
  }

  public void buildComponent() {
    getJComponent().setBounds(x, y, dimension.width, dimension.height);
  }

  public abstract JComponent getJComponent();

  public ResourceManager getResourceManager() {
    return Perceptionallity.getGame().getResourceManager();
  }
}
