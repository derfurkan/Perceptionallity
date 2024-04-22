package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.animation.InterpolationType;
import de.furkan.perceptionallity.animation.ValueIterator;
import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.MenuButton;
import de.furkan.perceptionallity.menu.components.MenuButtonClick;
import de.furkan.perceptionallity.menu.components.MenuLabel;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainMenu extends Menu {

  // Menu Animations
  private final ValueIterator titleInAnimation;

  private final ValueIterator subTitleFadeAnimation =
      new ValueIterator(1.0f, 0.4f, 0.02f, InterpolationType.DEFAULT);

  private final ValueIterator buttonInAnimation =
      new ValueIterator(-280, 20, 25, InterpolationType.SMOOTH_END);

  // Menu Titles
  private final String title = "Perceptionallity", subTitle = "A game by Furkan";

  // Components that are being animated (updated)
  MenuLabel titleLabel = new MenuLabel(0, 0, title, 50, Color.WHITE);
  int[] centerTitle = centerLocation(titleLabel.getDimension());
  MenuLabel subTitleLabel = new MenuLabel(0, 0, subTitle, 20, Color.WHITE);
  int[] centerSubTitle = centerLocation(subTitleLabel.getDimension());

  MenuButton playButton = new MenuButton(0, 220, 50, "PLAY");
  MenuButton optionsButton = new MenuButton(0, 0, 50, "OPTIONS");
  MenuButton githubButton = new MenuButton(0, 0, 50, "GITHUB");
  MenuButton exitButton = new MenuButton(0, 0, 50, "EXIT");

  public MainMenu() {
    super(30, Color.BLACK);

    titleInAnimation =
        new ValueIterator(
            (float)
                -(titleLabel.getDimension().getHeight() + subTitleLabel.getDimension().getHeight()),
            10,
            6,
            InterpolationType.SMOOTH_END);

    optionsButton.setBelow(playButton);
    githubButton.setBelow(optionsButton);
    exitButton.setBelow(githubButton);
  }

  @Override
  public String getMenuName() {
    return "Main";
  }

  @Override
  public void initComponents() {
    //        addSteadyComponent(
    //            getResourceManager().getResource("main_menu_background",
    //     Sprite.class).getRawComponent(),
    //            0);
    MenuLabel credits =
        new MenuLabel(
            5, 0, "Build " + Perceptionallity.getGame().getBuildString(), 15, Color.WHITE);
    int[] cornerLocation = edgeLocation(credits.getDimension());
    credits.setXY(cornerLocation[0], cornerLocation[1]);
    credits.buildComponent();

    MenuLabel debugMode =
        new MenuLabel(
            5,
            0,
            "Debug " + (Perceptionallity.getGame().isDebug() ? "ON" : "OFF"),
            15,
            Color.WHITE);
    cornerLocation = edgeLocation(debugMode.getDimension());
    debugMode.setXY(
        cornerLocation[0], (int) (cornerLocation[1] - credits.getDimension().getHeight()));
    debugMode.buildComponent();

    addSteadyComponent(debugMode.getJComponent(), 1);
    addSteadyComponent(credits.getJComponent(), 1);
  }

  private void addButtonFunctionality() {
    playButton.setButtonClick(
        new MenuButtonClick() {
          @Override
          public void onClick() {}
        });

    optionsButton.setButtonClick(
        new MenuButtonClick() {
          @Override
          public void onClick() {
            Perceptionallity.getGame().getMenuManager().setCurrentMenu(new OptionsMenu());
            Perceptionallity.getGame().getMenuManager().drawCurrentMenu();
          }
        });

    githubButton.setButtonClick(
        new MenuButtonClick() {
          @Override
          public void onClick() {

            try {
              Desktop.getDesktop().browse(new URI("https://github.com/derfurkan/Perceptionallity"));
            } catch (IOException | URISyntaxException e) {
              throw new RuntimeException(e);
            }
          }
        });

    exitButton.setButtonClick(
        new MenuButtonClick() {
          @Override
          public void onClick() {
            System.exit(0);
          }
        });
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
      subTitleLabel.setAlpha(subTitleFadeAnimation.getCurrentValue());
      subTitleFadeAnimation.updateValue();
      if (subTitleFadeAnimation.isFinished()) {
        subTitleFadeAnimation.reverse();
      }

      // Button in animation start
      if (!buttonInAnimation.isFinished()) {
        playButton.setX((int) buttonInAnimation.getCurrentValue());
        optionsButton.setX((int) buttonInAnimation.getCurrentValue());
        githubButton.setX((int) buttonInAnimation.getCurrentValue());
        exitButton.setX((int) buttonInAnimation.getCurrentValue());

        playButton.buildComponent();
        optionsButton.buildComponent();
        githubButton.buildComponent();
        exitButton.buildComponent();

        buttonInAnimation.updateValue();
        addTempComponent(playButton.getJComponent(), 1);
        addTempComponent(optionsButton.getJComponent(), 1);
        addTempComponent(githubButton.getJComponent(), 1);
        addTempComponent(exitButton.getJComponent(), 1);
      } else {
        addSteadyComponent(playButton.getJComponent(), 1);
        addSteadyComponent(optionsButton.getJComponent(), 1);
        addSteadyComponent(githubButton.getJComponent(), 1);
        addSteadyComponent(exitButton.getJComponent(), 1);
        addButtonFunctionality();
      }
    }

    addTempComponent(subTitleLabel.getJComponent(), 1);
  }
}
