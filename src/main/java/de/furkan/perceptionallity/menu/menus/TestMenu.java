package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.MenuLabel;
import de.furkan.perceptionallity.menu.components.MenuSlider;
import de.furkan.perceptionallity.menu.components.MenuSliderChangeEvent;
import java.awt.*;
import javax.swing.*;

public class TestMenu extends Menu {

  public TestMenu() {
    super(30, Color.BLACK);
  }

  @Override
  public String getMenuName() {
    return "Test";
  }

  @Override
  public void initComponents() {

    MenuSlider menuSlider = new MenuSlider(20, 20, new Dimension(300, 50), 10, 0, 100, 5);
    menuSlider.buildComponent();
    MenuLabel value = new MenuLabel(0, 20, "5", 40, Color.WHITE);

    value.setAsideRight(menuSlider);
    value.setX(value.getX() + 10);
    value.buildComponent();

    menuSlider.setMenuSliderChangeEvent(
        new MenuSliderChangeEvent() {
          @Override
          public void onChange(JSlider jSlider) {
            value.setText(String.valueOf(jSlider.getValue()));
            value.recalculateDimension();
            value.buildComponent();
          }
        });

    addSteadyComponent(value.getJComponent(), 1);
    addSteadyComponent(menuSlider.getJComponent(), 1);
  }

  @Override
  public void onUpdate() {}
}
