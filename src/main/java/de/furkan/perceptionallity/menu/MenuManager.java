package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.menu.menus.MainMenu;
import de.furkan.perceptionallity.menu.menus.OptionsMenu;
import de.furkan.perceptionallity.menu.menus.StartMenu;
import de.furkan.perceptionallity.menu.menus.TestMenu;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class MenuManager extends Manager {

  private Menu currentMenu;

  @Override
  public void initialize() {
    getGamePanel().setLayout(null);
    getGamePanel().setOpaque(true);
  }

  public JLayeredPane getGamePanel() {
    return Perceptionallity.getGame().getGamePanel();
  }

  // Debug
  public void reloadCurrentMenu() {
    getLogger().warning("Reloading current Menu (" + currentMenu.getMenuName() + ")");
    getSoundEngine().stopAllAudio();
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


  public int[] centerLocation(Dimension dimension) {
    return new int[] {
      (Perceptionallity.getGame().getWINDOW_WIDTH() / 2) - (dimension.width / 2),
      (Perceptionallity.getGame().getWINDOW_HEIGHT() / 2)
          - (dimension.height
              / 2) // TODO: investigate why this calculation is not working as intended
    };
  }

  public int[] edgeLocation(Dimension dimension) {
    return new int[] {
      (Perceptionallity.getGame().getWINDOW_WIDTH()) - (dimension.width + 20),
      (Perceptionallity.getGame().getWINDOW_HEIGHT())
          - (dimension.height
              + 15) // TODO: investigate why this calculation is not working as intended
    };
  }
}
