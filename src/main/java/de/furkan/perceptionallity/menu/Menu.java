package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Perceptionallity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Menu {

    public abstract void init();

    public JPanel getMainPanel() {
        return Perceptionallity.getMenuManager().getMainPanel();
    }

    public List<Component> menuComponents = new ArrayList<>();

    public void drawMenu() {
        init();
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
