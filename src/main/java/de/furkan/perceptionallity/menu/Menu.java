package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.discord.RPCStates;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;
import lombok.Getter;

@Getter
public abstract class Menu {

  private final List<Component> updatingComponents = new ArrayList<>();
  private final List<Component> steadyComponents = new ArrayList<>();
  private final int msPerUpdate;
  private Timer updateTimer;
  private long updates = 0;

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
              try {
                onUpdate();
              } catch (Exception ex) {
                Perceptionallity.handleFatalException(ex);
              }
              getGamePanel().repaint();
              getGamePanel().revalidate();
              updates++;
            });
    updateTimer.start();
  }

  /**
   * Retrieves the unique name of the menu. This name is used to identify the menu and manage its
   * state.
   *
   * @return the name of the menu as a String.
   */
  public abstract String getMenuName();

  /**
   * Initializes the components of the menu. This method should set up all UI components that are
   * part of the menu. It is called every time the menu is drawn.
   */
  public abstract void initComponents() throws Exception;

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

  public void drawMenu() throws Exception {
    for (Component component : getGamePanel().getComponents()) {
      removeComponent(component);
    }
    initComponents();
    getLogger().info("Initialized components for menu (" + getMenuName() + ")");

    // Discord Pass
    Perceptionallity.getDiscordRPCHandler().setState(RPCStates.IN_MENU, getMenuName());
  }

  /**
   * Adds a component to the menu that remains steady across updates. Steady components are not
   * removed during the update cycle.
   *
   * @param component the component to be added.
   * @param order the z-order of the component where 0 is the topmost position.
   */
  public void addSteadyComponent(Component component, int order) {
    if (steadyComponents.contains(component)) {
      return;
    }
    steadyComponents.add(component);
    getGamePanel().add(component, order, 1);
  }

  /**
   * Adds a temporary component to the menu which will be removed on the next update call. This is
   * useful for components that should only exist for a single update cycle.
   *
   * @param component the component to be added.
   * @param order the z-order of the component where 0 is the topmost position.
   */
  public void addTempComponent(Component component, int order) {
    updatingComponents.add(component);
    getGamePanel().add(component, order, 1);
  }

  public void removeComponent(Component component) {
    steadyComponents.remove(component);
    getGamePanel().remove(component);
  }

  /**
   * Cleans up the menu by stopping the update timer and removing all components. This method should
   * be called when the menu is no longer in use to free up resources.
   */
  public void unLoadMenu() {
    if (updateTimer != null) updateTimer.stop();
    for (Component component : steadyComponents) {
      getGamePanel().remove(component);
    }
    for (Component component : updatingComponents) {
      getGamePanel().remove(component);
    }
    steadyComponents.clear();
    updatingComponents.clear();
    getGamePanel().repaint();
    getGamePanel().revalidate();
  }

  public float getMsElapsed() {
    return ((getUpdates() * getMsPerUpdate()));
  }

  public int getSecondsElapsed() {
    return (int) (getMsElapsed() / 1000);
  }

  /**
   * Handles the update logic for the menu. This method is called periodically according to the
   * msPerUpdate setting. It should contain logic to update the state or appearance of the menu.
   */
  public abstract void onUpdate();
}
