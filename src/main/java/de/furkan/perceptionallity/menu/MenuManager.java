package de.furkan.perceptionallity.menu;

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

    mainPanel.setLayout(null);

    mainFrame = new JFrame("Perceptionallity");
    mainFrame.setResizable(false);
    mainFrame.setContentPane(mainPanel);
    mainFrame.setBounds(50, 50, WINDOW_WIDTH, WINDOW_HEIGHT+30);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void drawCurrentMenu() {
    if (currentMenu == null) {
      throw new RuntimeException("No current menu set!");
    }
    currentMenu.drawMenu();
    mainFrame.setVisible(true);
  }
}
