package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.menu.Menu;
import java.awt.*;

public class OptionsMenu extends Menu {

  public OptionsMenu(int msPerUpdate) {
    super(msPerUpdate, Color.BLACK);
  }

  @Override
  public String getMenuName() {
    return "Options";
  }

  @Override
  public void initComponents() {}

  @Override
  public void onUpdate() {}
}
