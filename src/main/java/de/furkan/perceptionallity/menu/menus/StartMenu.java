package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.animation.InterpolationType;
import de.furkan.perceptionallity.animation.ValueIterator;
import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.MenuComponent;
import de.furkan.perceptionallity.menu.components.MenuLabel;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class StartMenu extends Menu {

  final HashMap<MenuComponent, ValueIterator> menuComponents = new HashMap<>();
  String startText =
      "We are all alone on life's journey, held\n captive by the limitations of human\n consciousness.";
  boolean fadeOutStarted = false;

  public StartMenu() {
    super(30, Color.BLACK);

    // Build menu components

    MenuComponent lastComponent = null;
    for (String word : startText.split(" ")) {
      MenuComponent component = new MenuLabel(40, 0, word, 30, Color.WHITE);
      component.setY(getMenuManager().centerLocation(component.getDimension())[1] - 40);
      if (lastComponent != null) {
        if (word.contains("\n")) {
          component.setBelow(lastComponent);
        } else {
          component.setAsideRight(lastComponent);
          component.setX(component.getX() + 15);
          component.setY(lastComponent.getY());
        }
      }

      lastComponent = component;
      component.setOpacity(0.0f);
      menuComponents.put(
          component,
          new ValueIterator(
              0f, 1f, new Random().nextFloat(0.01f, 0.1f), InterpolationType.DEFAULT));
    }
  }

  @Override
  public String getMenuName() {
    return "Start";
  }

  @Override
  public void initComponents() {}

  @Override
  public void onUpdate() {
    if (getMsElapsed() < 500) return;
    AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    menuComponents
        .values()
        .forEach(
            valueIterator -> {
              if (!valueIterator.isFinished()) atomicBoolean.set(true);
            });

    if (atomicBoolean.get()) {
      menuComponents.forEach(
          (menuComponent, valueIterator) -> {
            menuComponent.setOpacity(valueIterator.getCurrentValue());
            menuComponent.buildComponent();
            valueIterator.updateValue();
          });
    } else {
      if (getSecondsElapsed() >= 6) {
        if (!fadeOutStarted) {
          fadeOutStarted = true;
          menuComponents.values().forEach(ValueIterator::reverse);
        } else {
          if (getSecondsElapsed() >= 7) {
            getMenuManager().setCurrentMenu(new MainMenu());
            getMenuManager().drawCurrentMenu();
          }
        }
      }
    }
    menuComponents
        .keySet()
        .forEach(menuComponent -> addTempComponent(menuComponent.getJComponent(), 1));
  }
}
