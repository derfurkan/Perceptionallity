package de.furkan.perceptionallity.menu;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.resources.ResourceManager;
import de.furkan.perceptionallity.util.sprite.Sprite;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MenuManager {

  private final JLayeredPane mainPanel;
  private final JFrame mainFrame;

  private final int WINDOW_WIDTH = 900;
  private final int WINDOW_HEIGHT = 500;

  @Setter private Menu currentMenu;

  public MenuManager() {
    this.mainPanel = new MenuPanel();
    this.mainFrame = new JFrame("Perceptionallity");
  }
  
  public void initialize() {
    mainPanel.setLayout(null);
    mainFrame.setResizable(false);
    mainFrame.setContentPane(mainPanel);
    mainFrame.setBounds(50, 50, WINDOW_WIDTH, WINDOW_HEIGHT + 30);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setIconImage(getResourceManager().getResource("game_icon", Sprite.class).getRawImage());
  }
  
  public ResourceManager getResourceManager() {
    return Perceptionallity.getResourceManager();
  }


  public void drawCurrentMenu() {
    if (currentMenu == null) {
      throw new RuntimeException("No current menu set!");
    }
    currentMenu.drawMenu();
    mainFrame.setVisible(true);
  }
}
