package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;
import lombok.Getter;

public abstract class Menu {

  private final List<Component> updatingComponents = new ArrayList<>();
  private final List<Component> steadyComponents = new ArrayList<>();


  @Getter
  private final int msPerUpdate;

  private Timer updateTimer;

  @Getter
  private long updates = 0;

  public Menu(int msPerUpdate, Color backgroundColor) {
    this.msPerUpdate = msPerUpdate;
    getMainPanel().setBackground(backgroundColor);
    if (msPerUpdate == -1) {
      return;
    }

    updateTimer = new Timer(msPerUpdate, e -> {
      if (!Perceptionallity.getGame()
              .getMenuManager()
              .getCurrentMenu()
              .getMenuName()
              .equals(getMenuName())) {
        getLogger()
                .warning(
                        "Stopped updateTimer for Menu '"
                                + getMenuName()
                                + "' because another currentMenu was found. Please make sure to stop the timer manually before loading any new menus.");
        ((Timer)e.getSource()).stop();
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
      updates++;
    });
    updateTimer.start();
  }

  public abstract String getMenuName();

  public abstract void initComponents();

  public JLayeredPane getMainPanel() {
    return Perceptionallity.getGame().getMenuManager().getMainPanel();
  }

  public boolean isSteadyComponent(JComponent component) {
    return steadyComponents.contains(component);
  }

  public ResourceManager getResourceManager() {
    return Perceptionallity.getGame().getResourceManager();
  }

  public MenuManager getMenuManager() {
    return Perceptionallity.getGame().getMenuManager();
  }

  public Logger getLogger() {
    return Perceptionallity.getGame().getLogger();
  }

  public void drawMenu() {
    for (Component component : getMainPanel().getComponents()) {
      removeComponent(component);
    }
    initComponents();
    getLogger().info("Initialized components for menu (" + getMenuName() + ")");
  }

  public void addSteadyComponent(Component component, int order) {
    if (steadyComponents.contains(component)) {
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

  public void unLoadMenu() {
    updateTimer.stop();
    steadyComponents.clear();
    updatingComponents.clear();
    for (Component component : getMainPanel().getComponents()) {
      getMainPanel().remove(component);
    }
    getMainPanel().repaint();
    getMainPanel().revalidate();
  }

  public float getMsElapsed() {
    return ((getUpdates() * getMsPerUpdate()));
  }

  public int getSecondsElapsed() {
    return (int) (getMsElapsed() / 1000);
  }

  // This method will be called every msPerUpdate
  public abstract void onUpdate();

  public int[] centerLocation(Dimension dimension) {
    return new int[] {
      (Perceptionallity.getGame().getMenuManager().getWINDOW_WIDTH() / 2) - (dimension.width / 2),
      (Perceptionallity.getGame().getMenuManager().getWINDOW_HEIGHT() / 2)
          - (dimension.height
              / 2) // TODO: investigate why this calculation is not working as intended
    };
  }

  public int[] edgeLocation(Dimension dimension) {
    return new int[] {
      (Perceptionallity.getGame().getMenuManager().getWINDOW_WIDTH()) - (dimension.width + 20),
      (Perceptionallity.getGame().getMenuManager().getWINDOW_HEIGHT())
          - (dimension.height
              + 15) // TODO: investigate why this calculation is not working as intended
    };
  }
}
