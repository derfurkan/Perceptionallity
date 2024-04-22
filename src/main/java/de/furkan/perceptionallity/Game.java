package de.furkan.perceptionallity;

import de.furkan.perceptionallity.menu.MenuManager;
import de.furkan.perceptionallity.menu.menus.StartMenu;
import de.furkan.perceptionallity.menu.menus.TestMenu;
import de.furkan.perceptionallity.resources.ResourceManager;
import de.furkan.perceptionallity.util.font.GameFont;
import de.furkan.perceptionallity.util.sprite.SpriteBuilder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;
import lombok.Getter;

@Getter
public class Game {

  public boolean showDebugLines = false;
  private MenuManager menuManager;
  private ResourceManager resourceManager;
  private SpriteBuilder spriteBuilder;
  private Logger logger;

  public boolean isDebug() {
    return true;
  }

  public String getBuildString() {
    return "DEV";
  }

  public void startGame() {
    buildLogger();
    resourceManager = new ResourceManager();
    menuManager = new MenuManager();
    spriteBuilder = new SpriteBuilder();

    // Load resources
    loadResources();
    logger.info("Finished loading resources.");
    menuManager.initialize();
    menuManager.setCurrentMenu(isDebug() ? new TestMenu() : new StartMenu());
    menuManager.drawCurrentMenu();
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
