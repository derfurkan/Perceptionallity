package de.furkan.perceptionallity;

import de.furkan.perceptionallity.menu.MenuManager;
import de.furkan.perceptionallity.menu.menus.MainMenu;
import de.furkan.perceptionallity.menu.menus.StartMenu;
import de.furkan.perceptionallity.menu.menus.TestMenu;
import de.furkan.perceptionallity.resources.ResourceManager;
import de.furkan.perceptionallity.sound.SoundEngine;
import de.furkan.perceptionallity.sound.Sound;
import de.furkan.perceptionallity.util.font.GameFont;
import de.furkan.perceptionallity.util.sprite.Sprite;
import de.furkan.perceptionallity.util.sprite.SpriteBuilder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;
import lombok.Getter;

import javax.swing.*;

@Getter
public class Game {

  public boolean showDebugLines = false;
  private final MenuManager menuManager;
  private final ResourceManager resourceManager;
  private final SpriteBuilder spriteBuilder;
  private final SoundEngine soundEngine;
  private Logger logger;
  private JFrame gameFrame;

  private final int WINDOW_WIDTH = 900;
  private final int WINDOW_HEIGHT = 500;

  public boolean isDebug() {
    return false;
  }

  public String getBuildString() {
    return "DEV";
  }

  public Game() {
    resourceManager = new ResourceManager();
    menuManager = new MenuManager();
    spriteBuilder = new SpriteBuilder();
    soundEngine = new SoundEngine();
  }

  public void start() {
    buildLogger();
    loadResources();
    logger.info("Finished loading resources.");
    createGameFrame();
    logger.info("Finished creating game frame.");
    menuManager.initialize();
    menuManager.setCurrentMenu(isDebug() ? new TestMenu() : new MainMenu());
    menuManager.drawCurrentMenu();
  }

  private void createGameFrame() {
    this.gameFrame = new JFrame("Perceptionallity");
    gameFrame.setResizable(false);
    gameFrame.setContentPane(menuManager.getMainPanel());
    gameFrame.setBounds(50, 50, WINDOW_WIDTH, WINDOW_HEIGHT + 30);
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameFrame.setIconImage(
            getResourceManager().getResource("game_icon", Sprite.class).getRawImage());
    gameFrame.setVisible(true);

    gameFrame.addKeyListener(
            new KeyListener() {
              @Override
              public void keyTyped(KeyEvent e) {
                if (Perceptionallity.getGame().isDebug()) {
                  switch (e.getKeyChar()) {
                    case 'r' -> menuManager.reloadCurrentMenu();
                    case 'f' ->
                            Perceptionallity.getGame().showDebugLines =
                                    !Perceptionallity.getGame().showDebugLines;
                    case 'g' -> {
                      menuManager.setCurrentMenu(new StartMenu());
                      menuManager.drawCurrentMenu();
                    }
                  }
                }
              }

              @Override
              public void keyPressed(KeyEvent e) {}

              @Override
              public void keyReleased(KeyEvent e) {}
            });
  }

  private void loadResources() {
    //    resourceManager.registerResource(
    //        "main_menu_background",
    //        spriteBuilder.buildSprite(
    //            new Dimension(menuManager.getWINDOW_WIDTH(), menuManager.getWINDOW_HEIGHT()),
    //            "main_menu_background.png",
    //            "menu",
    //            "backgrounds"));

    resourceManager.registerResource(
        "menu_font", new GameFont(Font.TRUETYPE_FONT, "joystixmonospace.otf", "font"));

    resourceManager.registerResource(
        "game_icon",
        spriteBuilder.buildSprite(new Dimension(150, 150), "game_icon.png", "menu", "icon"));

    resourceManager.registerResource(
        "menu_button",
        spriteBuilder.buildSprite(new Dimension(512, 256), "button.png", "menu", "button"));

    resourceManager.registerResource("button_hover", new Sound("button_hover.wav","menu","button"));

    resourceManager.registerResource("menu_music", new Sound("menu_music.wav","menu","audio"));

  }

  private void buildLogger() {
    logger = Logger.getLogger("");

    for (Handler handler : logger.getHandlers()) {
      logger.removeHandler(handler);
    }

    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new CustomLogFormatter());
    logger.addHandler(consoleHandler);
    logger.setLevel(Level.INFO);
  }
}

class CustomLogFormatter extends Formatter {
  @Override
  public String format(LogRecord record) {
    return record.getLevel()
        + " "
        + millisToHMS(record.getMillis())
        + " : "
        + formatMessage(record)
        + System.lineSeparator();
  }

  public String millisToHMS(long millis) {
    return new SimpleDateFormat("HH:mm:ss").format(new Date(millis));
  }
}
