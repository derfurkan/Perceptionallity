package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.animation.InterpolationType;
import de.furkan.perceptionallity.animation.ValueIterator;
import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.MenuButton;
import de.furkan.perceptionallity.menu.components.MenuLabel;
import java.awt.*;
import java.net.URI;

public class MainMenu extends Menu {

  // Menu Animations
  private final ValueIterator titleInAnimation;

  private final ValueIterator subTitleFadeAnimation =
      new ValueIterator(1.0f, 0.4f, 0.02f, InterpolationType.DEFAULT);

  private final ValueIterator buttonInAnimation =
      new ValueIterator(-280, 20, 25, InterpolationType.SMOOTH_END);

  // Menu Title
  private final String title = "Perceptionallity", subTitle = "A game by Furkan";

  // Components that are being animated (updated)
  MenuLabel titleLabel = new MenuLabel(0, 0, title, 50, Color.WHITE);
  int[] centerTitle = getMenuManager().centerLocation(titleLabel.getDimension());
  MenuLabel subTitleLabel = new MenuLabel(0, 0, subTitle, 20, Color.WHITE);
  int[] centerSubTitle = getMenuManager().centerLocation(subTitleLabel.getDimension());

  // TODO: Make this better and less ew.. Maybe a method which takes an array and creates menu
  // components automatically?
  // TODO: Use arrow keys to navigate
  MenuButton newGameButton = new MenuButton(0, 180, 50, "NEW GAME");
  MenuButton continueButton = new MenuButton(0, 180, 50, "CONTINUE");
  MenuButton optionsButton = new MenuButton(0, 0, 50, "OPTIONS");
  MenuButton githubButton = new MenuButton(0, 0, 50, "GITHUB");
  MenuButton discordButton = new MenuButton(0, 0, 50, "DISCORD");
  MenuButton exitButton = new MenuButton(0, 0, 50, "EXIT");

  public MainMenu() throws Exception {
    super(30, Color.BLACK);

    titleInAnimation =
        new ValueIterator(
            (float)
                -(titleLabel.getDimension().getHeight() + subTitleLabel.getDimension().getHeight()),
            10,
            6,
            InterpolationType.SMOOTH_END);

    continueButton.setActive(false);
    continueButton.setOpacity(0.3f);

    continueButton.setBelow(newGameButton, 0);
    optionsButton.setBelow(continueButton, 0);
    githubButton.setBelow(optionsButton, 0);
    discordButton.setBelow(githubButton, 0);
    exitButton.setBelow(discordButton, 0);

    addButtonFunctionality();
  }

  @Override
  public String getMenuName() {
    return "Main";
  }

  @Override
  public void initComponents() throws Exception {

    MenuLabel credits =
        new MenuLabel(
            5, 0, "Build " + Perceptionallity.getGame().getBuildString(), 15, Color.WHITE);
    int[] cornerLocation = getMenuManager().edgeLocation(credits.getDimension());
    credits.setXY(cornerLocation[0], cornerLocation[1]);
    credits.buildComponent();

    MenuLabel debugMode =
        new MenuLabel(
            5,
            0,
            "Debug " + (Perceptionallity.getGame().isDebug() ? "ON" : "OFF"),
            15,
            Color.WHITE);
    cornerLocation = getMenuManager().edgeLocation(debugMode.getDimension());
    debugMode.setXY(
        cornerLocation[0], (int) (cornerLocation[1] - credits.getDimension().getHeight()));
    debugMode.buildComponent();

    addSteadyComponent(debugMode.getJComponent(), 1);
    addSteadyComponent(credits.getJComponent(), 1);
  }

  private void addButtonFunctionality() {
    newGameButton.setButtonClick(
        () -> {
          getMenuManager().getCurrentMenu().unLoadMenu();
          Perceptionallity.getGame().getGameManager().initialize();
        });

    optionsButton.setButtonClick(
        () -> {
          Perceptionallity.getGame().getMenuManager().setCurrentMenu(new OptionsMenu());
          Perceptionallity.getGame().getMenuManager().drawCurrentMenu();
        });

    githubButton.setButtonClick(
        () ->
            Desktop.getDesktop().browse(new URI("https://github.com/derfurkan/Perceptionallity")));

    exitButton.setButtonClick(() -> System.exit(0));

    discordButton.setButtonClick(
        () -> Desktop.getDesktop().browse(new URI("https://discord.gg/pF5FnwDxBj")));
  }

  @Override
  public void onUpdate() {
    if (!titleInAnimation.isFinished()) {
      // Titles move in animation
      titleLabel.setX(centerTitle[0]);
      titleLabel.setY((int) titleInAnimation.getCurrentValue());

      subTitleLabel.setX(centerSubTitle[0]);
      subTitleLabel.setY(
          (int) titleInAnimation.getCurrentValue() + titleLabel.getDimension().height);

      titleLabel.buildComponent();
      subTitleLabel.buildComponent();

      titleInAnimation.updateValue();

      addTempComponent(titleLabel.getJComponent(), 1);
    } else {
      addSteadyComponent(titleLabel.getJComponent(), 1);

      // SubTitle glow animation start
      subTitleLabel.setOpacity(subTitleFadeAnimation.getCurrentValue());
      subTitleFadeAnimation.updateValue();
      if (subTitleFadeAnimation.isFinished()) {
        subTitleFadeAnimation.reverse();
      }

      // Button in animation start
      if (!buttonInAnimation.isFinished()) {
        newGameButton.setX((int) buttonInAnimation.getCurrentValue());
        continueButton.setX((int) buttonInAnimation.getCurrentValue());
        optionsButton.setX((int) buttonInAnimation.getCurrentValue());
        githubButton.setX((int) buttonInAnimation.getCurrentValue());
        discordButton.setX((int) buttonInAnimation.getCurrentValue());
        exitButton.setX((int) buttonInAnimation.getCurrentValue());

        newGameButton.buildComponent();
        continueButton.buildComponent();
        optionsButton.buildComponent();
        githubButton.buildComponent();
        discordButton.buildComponent();
        exitButton.buildComponent();

        buttonInAnimation.updateValue();
        addTempComponent(newGameButton.getJComponent(), 1);
        addTempComponent(continueButton.getJComponent(), 1);
        addTempComponent(optionsButton.getJComponent(), 1);
        addTempComponent(githubButton.getJComponent(), 1);
        addTempComponent(discordButton.getJComponent(), 1);
        addTempComponent(exitButton.getJComponent(), 1);
      } else {
        addSteadyComponent(newGameButton.getJComponent(), 1);
        addSteadyComponent(continueButton.getJComponent(), 1);
        addSteadyComponent(optionsButton.getJComponent(), 1);
        addSteadyComponent(githubButton.getJComponent(), 1);
        addSteadyComponent(discordButton.getJComponent(), 1);
        addSteadyComponent(exitButton.getJComponent(), 1);
      }
    }
    addTempComponent(subTitleLabel.getJComponent(), 1);
  }
}
