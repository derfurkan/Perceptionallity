package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.animation.InterpolationType;
import de.furkan.perceptionallity.animation.ValueIterator;
import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.MenuLabel;
import java.awt.*;

public class MainMenu extends Menu {

  // Menu Animations
  private final ValueIterator titleInAnimation =
      new ValueIterator(-90, 10, 6, InterpolationType.SMOOTH_END);

  private final ValueIterator subTitleFadeAnimation =
      new ValueIterator(1.0f, 0.4f, 0.03f, InterpolationType.DEFAULT);

  // Menu Titles
  private final String title = "Perceptionallity", subTitle = "A game by Furkan";

  // Components that are being animated (updated)
  MenuLabel titleLabel = new MenuLabel(0, 0, title, 50, Color.WHITE);
  int[] centerTitle = centerLocation(titleLabel.getDimension());
  MenuLabel subTitleLabel = new MenuLabel(0, 0, subTitle, 20, Color.WHITE);
  int[] centerSubTitle = centerLocation(subTitleLabel.getDimension());

  public MainMenu() {
    super(50, Color.BLACK);
  }

  @Override
  public String getMenuName() {
    return "Main";
  }

  @Override
  public void initComponents() {
    //    addSteadyComponent(
    //        getResourceManager().getResource("main_menu_background",
    // Sprite.class).getRawComponent(),
    //        0);
    MenuLabel credits = new MenuLabel(5, 0, "Build " + Perceptionallity.BUILD, 15, Color.WHITE);
    int[] cornerLocation = cornerLocation(credits.getDimension());
    credits.setXY(cornerLocation[0], cornerLocation[1]);
    credits.buildLabel();

    MenuLabel debugMode =
        new MenuLabel(
            5, 0, "Debug " + (Perceptionallity.DEBUG_MODE ? "ON" : "OFF"), 15, Color.WHITE);
    cornerLocation = cornerLocation(debugMode.getDimension());
    debugMode.setXY(
        cornerLocation[0], (int) (cornerLocation[1] - credits.getDimension().getHeight()));
    debugMode.buildLabel();

    addSteadyComponent(debugMode.getLabel(), 1);
    addSteadyComponent(credits.getLabel(), 1);
  }

  @Override
  public void onUpdate() {

    // Titles move in animation
    titleLabel.setX(centerTitle[0]);
    titleLabel.setY((int) titleInAnimation.getCurrentValue());

    subTitleLabel.setX(centerSubTitle[0]);
    subTitleLabel.setY((int) titleInAnimation.getCurrentValue() + titleLabel.getDimension().height);

    titleLabel.buildLabel();
    subTitleLabel.buildLabel();

    titleInAnimation.updateValue();

    if (!titleInAnimation.isFinished()) {
      addTempComponent(titleLabel.getLabel(), 1);
    } else {
      addSteadyComponent(titleLabel.getLabel(), 1);

      // SubTitle glow animation start
      subTitleLabel.setAlpha(subTitleFadeAnimation.getCurrentValue());
      subTitleFadeAnimation.updateValue();
      if (subTitleFadeAnimation.isFinished()) {
        subTitleFadeAnimation.reverse();
      }
    }

    addTempComponent(subTitleLabel.getLabel(), 1);
  }
}
