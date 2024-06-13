package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    setY(menuComponent.getY() - menuComponent.getDimension().height); // + getx
  }

  public void setBelow(MenuComponent menuComponent, int additional) {
    setY((menuComponent.getY() + menuComponent.getDimension().height) + additional);
  }

  public void setAsideRight(MenuComponent menuComponent) {
    setX(menuComponent.getX() + menuComponent.getDimension().width); // + getx
  }

  public void setAsideLeft(MenuComponent menuComponent) {
    setX(menuComponent.getX() - menuComponent.getDimension().width); // + getx
  }

  public void setSameHeight(MenuComponent menuComponent) {
    setY(menuComponent.getY());
  }

  public void setXY(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public float getOpacity() {
    Color color = getJComponent().getForeground();
    return (float) color.getAlpha() / 255;
  }

  public void setOpacity(float alpha) {
    if (this instanceof MenuSprite menuSprite) {
      Image originalImage = menuSprite.getSprite().getRawImage();
      BufferedImage bufferedImage =
          new BufferedImage(
              originalImage.getWidth(null),
              originalImage.getHeight(null),
              BufferedImage.TYPE_INT_ARGB);

      Graphics2D g2d = bufferedImage.createGraphics();
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
      g2d.drawImage(originalImage, 0, 0, null);
      g2d.dispose();

      ((JLabel) menuSprite.getJComponent()).setIcon(new ImageIcon(bufferedImage));
    }

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
