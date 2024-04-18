package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.resources.ResourceManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;

public abstract class Menu {

  public List<Component> menuComponents = new ArrayList<>();

  public abstract String getMenuName();

  public abstract void init();

  public JPanel getMainPanel() {
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
    init();
    getLogger()
        .info("Drawing menu: " + getMenuName() + " with " + menuComponents.size() + " components.");
    for (Component component : getMainPanel().getComponents()) {
      component.setVisible(false);
    }
    for (Component component : menuComponents) {
      component.setVisible(true);
    }
    getMainPanel().repaint();
    getMainPanel().revalidate();
  }

  public void addComponent(Component component) {
    getMainPanel().add(component);
    menuComponents.add(component);
  }

  public void removeComponent(Component component) {
    getMainPanel().remove(component);
    menuComponents.remove(component);
  }
}
