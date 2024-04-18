package de.furkan.perceptionallity.menu;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
public class MenuManager {

    private final JPanel mainPanel;
    private final JFrame mainFrame;

    private final int WINDOW_WIDTH = 900;
    private final int WINDOW_HEIGHT = 500;

    @Setter
    private Menu currentMenu;


    public MenuManager() {
        this.mainPanel = new JPanel();

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        mainFrame = new JFrame("Perceptionality");
        mainFrame.setResizable(false);
        mainFrame.setContentPane(mainPanel);
        mainFrame.setBounds(50,50,WINDOW_WIDTH,WINDOW_HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public void drawCurrentMenu() {
        if(currentMenu == null) {
            throw new RuntimeException("No current menu set!");
        }
        currentMenu.drawMenu();
    }


}
