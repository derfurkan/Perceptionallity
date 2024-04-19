package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Logger;
import javax.swing.*;

public abstract class Menu {

  private final List<Component> updatingComponents = new ArrayList<>();
  private final List<Component> steadyComponents = new ArrayList<>();

  public Menu(int msPerUpdate) {
    if (msPerUpdate == -1) {
      return;
    }
    java.util.Timer updateTimer = new java.util.Timer(getMenuName() + "-updateTask", false);
    updateTimer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            if (!Perceptionallity.getMenuManager()
                .getCurrentMenu()
                .getMenuName()
                .equals(getMenuName())) {
              getLogger()
                  .warning(
                      "Stopped updateTimer for Menu '"
                          + getMenuName()
                          + "' because another currentMenu was found. Please make sure to stop the timer manually before loading any new menus.");
              cancel();
              return;
            }
            new ArrayList<>(updatingComponents)
                .forEach(
                    o -> {
                      updatingComponents.remove(o);
                      getMainPanel().remove(o);
                    });
            onUpdate();
            getMainPanel().repaint();
            getMainPanel().revalidate();
          }
        },
        1000,
        msPerUpdate);
  }

  public abstract String getMenuName();

  public abstract void onInit();

  public JLayeredPane getMainPanel() {
    return Perceptionallity.getMenuManager().getMainPanel();
  }

  public ResourceManager getResourceManager() {
    return Perceptionallity.getResourceManager();
  }

  public Logger getLogger() {
    return Perceptionallity.getLogger();
  }

  public void drawMenu() {
    getLogger().info("Initializing menu: " + getMenuName());
    for (Component component : getMainPanel().getComponents()) {
      removeComponent(component);
    }
    onInit();
    getLogger()
        .info(
            "Drawing menu: "
                + getMenuName()
                + " with "
                + getMainPanel().getComponents().length
                + " components.");
  }

  public void addSteadyComponent(Component component, int order) {
    if(steadyComponents.contains(component)) {
      return;
    }
    steadyComponents.add(component);
    getMainPanel().add(component, order, 0);
  }

  // Adding a component which will be removed on the next update call.
  public void addTempComponent(Component component, int order) {
    updatingComponents.add(component);
    getMainPanel().add(component, order, 0);
  }

  public void removeComponent(Component component) {
    steadyComponents.remove(component);
    getMainPanel().remove(component);
  }

  // This method will be called every msPerUpdate
  public abstract void onUpdate();

  public int[] centerLocation(Dimension dimension) {
    return new int[] {
      (Perceptionallity.getMenuManager().getWINDOW_WIDTH() / 2) - (dimension.width / 2),
      (Perceptionallity.getMenuManager().getWINDOW_HEIGHT() / 2)
          - (dimension.height
              / 2) // TODO: investigate why this calculation is not working as intended
    };
  }

  public int[] cornerLocation(Dimension dimension) {
    return new int[] {
            (Perceptionallity.getMenuManager().getWINDOW_WIDTH()) - (dimension.width+20),
            (Perceptionallity.getMenuManager().getWINDOW_HEIGHT()) - (dimension.height+15) // TODO: investigate why this calculation is not working as intended
    };
  }

}
