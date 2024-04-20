package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.menu.menus.MainMenu;
import de.furkan.perceptionallity.resources.ResourceManager;
import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;
import javax.swing.*;
import lombok.Getter;

@Getter
public class MenuManager {

  private final JLayeredPane mainPanel;
  private final JFrame mainFrame;

  private final int WINDOW_WIDTH = 900;
  private final int WINDOW_HEIGHT = 500;

  private Menu currentMenu;

  public MenuManager() {
    this.mainPanel = new MenuPanel();
    this.mainFrame = new JFrame("Perceptionallity");
  }

  public void initialize() {
    mainPanel.setLayout(null);
    mainPanel.setOpaque(true);
    mainFrame.setResizable(false);
    mainFrame.setContentPane(mainPanel);
    mainFrame.setBounds(50, 50, WINDOW_WIDTH, WINDOW_HEIGHT + 30);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setIconImage(
        getResourceManager().getResource("game_icon", Sprite.class).getRawImage());

    mainFrame.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            if(Perceptionallity.getGame().isDebug()) {
              switch (e.getKeyChar()) {
                case 'r' -> reloadCurrentMenu();
                case 'f' -> Perceptionallity.getGame().showDebugLines = !Perceptionallity.getGame().showDebugLines;
              }
            }
  
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });
  }

  // Debug
  private void reloadCurrentMenu() {
    getLogger().warning("Reloading current Menu (" + currentMenu.getMenuName() + ")");
    Menu newMenu = null;

    // Ew
    if (currentMenu instanceof MainMenu) newMenu = new MainMenu();

    if (newMenu == null) {
      throw new RuntimeException(
              "Unknown Menu to reload. Please specify a class for the menu. ("
                      + currentMenu.getMenuName()
                      + ")");
    }

    setCurrentMenu(newMenu);
    drawCurrentMenu();
  }

  public ResourceManager getResourceManager() {
    return Perceptionallity.getGame().getResourceManager();
  }

  public void drawCurrentMenu() {
    if (currentMenu == null) {
      throw new RuntimeException("No current menu set!");
    }
    currentMenu.drawMenu();
    mainFrame.setVisible(true);
  }

  public Logger getLogger() {
    return Perceptionallity.getGame().getLogger();
  }

  public void setCurrentMenu(Menu currentMenu) {
    if (this.currentMenu != null) this.currentMenu.unLoadMenu();
    this.currentMenu = currentMenu;
  }
}
