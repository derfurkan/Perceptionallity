package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.menu.Menu;
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
    getMenuManager().getCurrentMenu().unLoadMenu();
    Perceptionallity.getGame().getGameManager().initialize();
  }

  @Override
  public void onUpdate() {}
}
