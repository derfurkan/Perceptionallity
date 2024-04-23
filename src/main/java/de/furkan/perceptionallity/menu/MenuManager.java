package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.GamePanel;
import de.furkan.perceptionallity.menu.menus.MainMenu;
import de.furkan.perceptionallity.menu.menus.OptionsMenu;
import de.furkan.perceptionallity.menu.menus.StartMenu;
import de.furkan.perceptionallity.menu.menus.TestMenu;

import javax.swing.*;
import lombok.Getter;

@Getter
public class MenuManager extends Manager {


  private final JLayeredPane mainPanel;


  private Menu currentMenu;

  public MenuManager() {
    this.mainPanel = new GamePanel();
  }

  @Override
  public void initialize() {
    mainPanel.setLayout(null);
    mainPanel.setOpaque(true);
  }

  // Debug
  public void reloadCurrentMenu() {
    getLogger().warning("Reloading current Menu (" + currentMenu.getMenuName() + ")");
    getSoundManager().stopAllAudio();
    Menu newMenu = null;

    // Ew
    if (currentMenu instanceof MainMenu) newMenu = new MainMenu();
    else if (currentMenu instanceof OptionsMenu) newMenu = new OptionsMenu();
    else if (currentMenu instanceof StartMenu) newMenu = new StartMenu();
    else if (currentMenu instanceof TestMenu) newMenu = new TestMenu();

    //    newMenu =
    // Class.forName(currentMenu.getClass().getModule(),currentMenu.getClass().getName());

    if (newMenu == null) {
      throw new RuntimeException(
          "Unknown Menu to reload. Please specify a class for the menu. ("
              + currentMenu.getMenuName()
              + ")");
    }

    setCurrentMenu(newMenu);
    drawCurrentMenu();
  }


  public void drawCurrentMenu() {
    if (currentMenu == null) {
      throw new RuntimeException("No current menu set!");
    }
    currentMenu.drawMenu();
  }


  public void setCurrentMenu(Menu currentMenu) {
    if (this.currentMenu != null) this.currentMenu.unLoadMenu();
    this.currentMenu = currentMenu;
  }
}
