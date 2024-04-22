package de.furkan.perceptionallity.menu.components;

import lombok.Setter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MenuSlider extends MenuComponent {

  private final JSlider rawComponent;
  @Setter private MenuSliderChangeEvent menuSliderChangeEvent;

  public MenuSlider(int x, int y, Dimension dimension, int size, int min, int max, int current) {
    super(x, y, dimension);
    rawComponent = new JSlider();
    rawComponent.setUI(new CustomSliderUI(size));
    rawComponent.setMinimum(min);
    rawComponent.setMaximum(max);
    rawComponent.setOpaque(false);
    rawComponent.setFocusable(false);
    rawComponent.setValue(current);
    rawComponent.addChangeListener(
        new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            menuSliderChangeEvent.onChange(rawComponent);
          }
        });
  }

  @Override
  public JComponent getJComponent() {
    return rawComponent;
  }
}

class CustomSliderUI extends BasicSliderUI {

  private final RoundRectangle2D.Float trackShape = new RoundRectangle2D.Float();
  private final int size;

  public CustomSliderUI(int size) {
    this.size = size;
  }

  @Override
  public void paintTrack(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    Shape clip = g2.getClip();

    boolean inverted = slider.getInverted();

    g2.setColor(new Color(33, 33, 33));
    g2.fill(trackShape);

    boolean ltr = slider.getComponentOrientation().isLeftToRight();
    if (ltr) inverted = !inverted;
    int thumbPos = thumbRect.x + thumbRect.width / 2;
    if (inverted) {
      g2.clipRect(0, 0, thumbPos, slider.getHeight());
    } else {
      g2.clipRect(thumbPos, 0, slider.getWidth() - thumbPos, slider.getHeight());
    }

    g2.setColor(Color.WHITE);
    g2.fill(trackShape);
    g2.setClip(clip);
  }

  @Override
  protected void calculateThumbLocation() {
    super.calculateThumbLocation();
    thumbRect.y = trackRect.y + (trackRect.height - thumbRect.height) / 2;
  }

  @Override
  public void paintThumb(final Graphics g) {
    g.setColor(new Color(255, 255, 255));
    g.fillRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
  }

  @Override
  protected void calculateTrackRect() {
    super.calculateTrackRect();

    trackRect.y = trackRect.y + (trackRect.height - size) / 2;
    trackRect.height = size;

    trackShape.setRoundRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height, 0, 0);
  }

  @Override
  protected Dimension getThumbSize() {
    return new Dimension(size, size + 20);
  }

  @Override
  public void paint(final Graphics g, final JComponent c) {
    ((Graphics2D) g)
        .setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    super.paint(g, c);
  }
}
