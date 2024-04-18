package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.util.sprite.Sprite;

public class MainMenu extends Menu {

  @Override
  public String getMenuName() {
    return "Main";
  }

  @Override
  public void init() {
    addComponent(
        getResourceManager().getResource("main_menu_background", Sprite.class).getRawComponent());
  }
}
