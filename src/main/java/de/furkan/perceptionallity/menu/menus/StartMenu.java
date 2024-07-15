package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.animation.InterpolationType;
import de.furkan.perceptionallity.animation.ValueIterator;
import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.label.MenuLabel;
import java.awt.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class StartMenu extends Menu {

  private final HashMap<MenuLabel, ValueIterator> textComponents = new HashMap<>();
  String startText =
      "We are all alone on life's journey, held\n captive by the limitations of human\n consciousness.";

  boolean fadeOutStarted = false;

  public StartMenu() {
    super(30, Color.BLACK);

    // Build menu components

    MenuLabel lastComponent = null;
    for (String word : startText.split(" ")) {
      MenuLabel component = new MenuLabel(40, 0, word, 70, Color.WHITE);

      component.setY(getMenuManager().centerLocation(component.getDimension())[1] - 40);
      if (lastComponent != null) {
        if (word.contains("\n")) {
          component.setBelow(lastComponent, 20);
        } else {
          component.setAsideRight(lastComponent, 30);
          component.setY(lastComponent.getY());
        }
      }

      lastComponent = component;
      component.setOpacity(0.0f);
      textComponents.put(
          component,
          new ValueIterator(
              0f, 1f, new Random().nextFloat(0.007f, 0.015f), InterpolationType.DEFAULT));
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

    textComponents
        .values()
        .forEach(
            valueIterator -> {
              if (!valueIterator.isFinished()) atomicBoolean.set(true);
            });

    if (atomicBoolean.get()) {
      textComponents.forEach(
          (menuComponent, valueIterator) -> {
            menuComponent.setOpacity(valueIterator.getCurrentValue());
            menuComponent.buildComponent();
            valueIterator.updateValue();
          });
    } else {
      if (getSecondsElapsed() >= 8) {
        if (!fadeOutStarted) {
          fadeOutStarted = true;
          textComponents
              .values()
              .forEach(
                  valueIterator -> {
                    valueIterator.setStep(
                        new Random()
                            .nextFloat(valueIterator.getStep(), valueIterator.getStep() + 0.015f));
                    valueIterator.reverse();
                  });
        } else {
          if (getSecondsElapsed() >= 9) {
            getMenuManager().setCurrentMenu(new MainMenu());
            getMenuManager().drawCurrentMenu();
          }
        }
      }
    }
    textComponents
        .keySet()
        .forEach(menuComponent -> addTempComponent(menuComponent.getJComponent(), 1));
  }
}
