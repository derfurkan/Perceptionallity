package de.furkan.perceptionallity;

import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.game.GameManager;
import de.furkan.perceptionallity.game.GamePanel;
import de.furkan.perceptionallity.menu.MenuManager;
import de.furkan.perceptionallity.menu.menus.StartMenu;
import de.furkan.perceptionallity.menu.menus.TestMenu;
import de.furkan.perceptionallity.resources.ResourceManager;
import de.furkan.perceptionallity.sound.Sound;
import de.furkan.perceptionallity.sound.SoundEngine;
import de.furkan.perceptionallity.util.font.GameFont;
import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class Game {

  private final MenuManager menuManager;
  private final GameManager gameManager;
  private final ResourceManager resourceManager;
  private final SoundEngine soundEngine;
  private final GamePanel gamePanel = new GamePanel();
  private final int WINDOW_WIDTH = 900;
  private final int WINDOW_HEIGHT = 500;
  private final Dimension windowDimension = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
  public boolean showDebugLines = false;
  private Logger logger;
  private JFrame gameFrame;

  public Game() {
    resourceManager = new ResourceManager();
    menuManager = new MenuManager();
    gameManager = new GameManager();
    soundEngine = new SoundEngine();
  }

  public boolean isDebug() {
    return true;
  }

  public String getBuildString() {
    return "DEV-0.03";
  }

  /**
   * Initializes and starts the game by setting up the logger, loading resources, creating the game
   * frame, and setting the initial menu. It decides the initial menu based on whether the game is
   * in debug mode.
   */
  public void start() {
    buildLogger();
    try {
      loadResources();
    } catch (Exception e) {
      getLogger().severe("Error while loading resources");
      e.printStackTrace();
      return;
    }
    logger.info("Finished loading resources");
    createGameFrame();
    logger.info("Finished creating game frame");
    menuManager.initialize();

    menuManager.setCurrentMenu(isDebug() ? new TestMenu() : new StartMenu());

    menuManager.drawCurrentMenu();
  }

  /**
   * Creates the main game window, sets its properties, and initializes key listeners for debug
   * commands. The game frame is set to be non-resizable and will close the application on exit. Key
   * listeners support debug functionalities like reloading menus and toggling debug lines.
   */
  private void createGameFrame() {
    this.gameFrame = new JFrame("Perceptionallity");
    gameFrame.setResizable(false);
    gameFrame.setContentPane(menuManager.getGamePanel());
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

  /**
   * Loads all necessary resources such as fonts, sprites, and sounds into the game. Resources are
   * registered with unique keys and are loaded from specified paths. This method is crucial for
   * preparing all visual and audio assets needed for the game.
   */
  private void loadResources() {
    resourceManager.registerResource(
        "menu_font", new GameFont(Font.TRUETYPE_FONT, "joystixmonospace.otf", "font"));

    resourceManager.registerResource("game_icon", new Sprite("game_icon.png", "menu", "icon"));

    resourceManager.registerResource("menu_music", new Sound("menu_music.wav", "menu", "audio"));

    registerAnimationResource(
        "player_idle_down_animation",
        2,
        5,
        true,
        "IdleDown.png",
        "game",
        "player",
        "animation_sheets");

    registerAnimationResource(
        "player_idle_up_animation", 2, 5, true, "IdleUp.png", "game", "player", "animation_sheets");

    registerAnimationResource(
        "player_idle_left_animation",
        2,
        5,
        true,
        "IdleLeft.png",
        "game",
        "player",
        "animation_sheets");

    registerAnimationResource(
        "player_idle_right_animation",
        2,
        5,
        true,
        "IdleRight.png",
        "game",
        "player",
        "animation_sheets");

    registerAnimationResource(
        "player_walk_down_animation",
        4,
        8,
        true,
        "WalkDown.png",
        "game",
        "player",
        "animation_sheets");
    registerAnimationResource(
        "player_walk_up_animation", 4, 8, true, "WalkUp.png", "game", "player", "animation_sheets");
    registerAnimationResource(
        "player_walk_left_animation",
        4,
        8,
        true,
        "WalkLeft.png",
        "game",
        "player",
        "animation_sheets");
    registerAnimationResource(
        "player_walk_right_animation",
        4,
        8,
        true,
        "WalkRight.png",
        "game",
        "player",
        "animation_sheets");
  }

  private void registerAnimationResource(
      String animationKey,
      int columns,
      int fps,
      boolean loop,
      String sheetFile,
      String... sheetPath) {

    resourceManager.registerResource(
        animationKey + "_sheet", new Sprite(resourceManager.getResourceFile(sheetFile, sheetPath)));

    resourceManager.registerResource(
        animationKey,
        new Animation(
            resourceManager.cutSpriteSheet(
                resourceManager.getResource(animationKey + "_sheet", Sprite.class), 1, columns),
            fps,
            loop));
  }

  /**
   * Configures the logger for the game. It removes all default handlers and adds a custom console
   * handler. This method sets up the logger to output information about game operations, aiding in
   * debugging and monitoring game state.
   */
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
