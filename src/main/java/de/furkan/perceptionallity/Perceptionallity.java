package de.furkan.perceptionallity;

import de.furkan.perceptionallity.logger.CustomLogFormatter;
import de.furkan.perceptionallity.menu.MenuManager;
import de.furkan.perceptionallity.menu.menus.MainMenu;
import de.furkan.perceptionallity.resources.ResourceManager;
import de.furkan.perceptionallity.util.font.GameFont;
import de.furkan.perceptionallity.util.sprite.SpriteBuilder;
import java.awt.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;

public class Perceptionallity {

  public static final boolean DEBUG_MODE = true;

  @Getter private static MenuManager menuManager;

  @Getter private static ResourceManager resourceManager;

  @Getter private static SpriteBuilder spriteBuilder;

  @Getter private static Logger logger;

  // In case we need this for later.
  @Getter private static MainMenu mainMenu;

  public static void main(String[] args) {
    buildLogger();
    menuManager = new MenuManager();
    spriteBuilder = new SpriteBuilder();
    resourceManager = new ResourceManager();

    // Load resources
    loadResources();
    logger.info("Finished loading resources.");

    menuManager.setCurrentMenu(mainMenu = new MainMenu(50));
    menuManager.drawCurrentMenu();
  }

  private static void loadResources() {
    resourceManager.registerResource(
        "main_menu_background",
        spriteBuilder.buildSprite(
            new Dimension(menuManager.getWINDOW_WIDTH(), menuManager.getWINDOW_HEIGHT()),
            "main_menu_background.png",
            "menu",
            "backgrounds"));

    resourceManager.registerResource(
        "menu_font", new GameFont(Font.TRUETYPE_FONT, "dpcomic.ttf", "font"));
  }

  private static void buildLogger() {
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
