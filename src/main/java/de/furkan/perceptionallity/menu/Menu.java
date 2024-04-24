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

  @Getter private final int msPerUpdate;

  private Timer updateTimer;

  @Getter private long updates = 0;

  public Menu(int msPerUpdate, Color backgroundColor) {
    this.msPerUpdate = msPerUpdate;
    getGamePanel().setBackground(backgroundColor);
    if (msPerUpdate == -1) {
      return;
    }

    updateTimer =
        new Timer(
            msPerUpdate,
            e -> {
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
                ((Timer) e.getSource()).stop();
                return;
              }
              new ArrayList<>(updatingComponents)
                  .forEach(
                      o -> {
                        updatingComponents.remove(o);
                        getGamePanel().remove(o);
                      });
              onUpdate();
              getGamePanel().repaint();
              getGamePanel().revalidate();
              updates++;
            });
    updateTimer.start();
  }

  public abstract String getMenuName();

  public abstract void initComponents();

  public JLayeredPane getGamePanel() {
    return Perceptionallity.getGame().getMenuManager().getGamePanel();
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
    for (Component component : getGamePanel().getComponents()) {
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
    getGamePanel().add(component, order, 0);
  }

  // Adding a component which will be removed on the next update call.
  public void addTempComponent(Component component, int order) {
    updatingComponents.add(component);
    getGamePanel().add(component, order, 0);
  }

  public void removeComponent(Component component) {
    steadyComponents.remove(component);
    getGamePanel().remove(component);
  }

  public void unLoadMenu() {
    updateTimer.stop();
    steadyComponents.clear();
    updatingComponents.clear();
    for (Component component : getGamePanel().getComponents()) {
      getGamePanel().remove(component);
    }
    getGamePanel().repaint();
    getGamePanel().revalidate();
  }

  public float getMsElapsed() {
    return ((getUpdates() * getMsPerUpdate()));
  }

  public int getSecondsElapsed() {
    return (int) (getMsElapsed() / 1000);
  }

  // This method will be called every msPerUpdate
  public abstract void onUpdate();
}
