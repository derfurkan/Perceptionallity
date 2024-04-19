package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.animation.InterpolationType;
import de.furkan.perceptionallity.animation.ValueIterator;
import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.MenuLabel;
import de.furkan.perceptionallity.util.sprite.Sprite;

import java.awt.*;

public class MainMenu extends Menu {

  private final ValueIterator titleInAnimation =
      new ValueIterator(-90, 20, 10, InterpolationType.SMOOTH_END);

  private ValueIterator titleGlowAnimation =
          new ValueIterator(0.9f, 0.3f, 0.03f, InterpolationType.DEFAULT);

  private MenuLabel title;

  public MainMenu(int msPerUpdate) {
    super(msPerUpdate);
  }

  @Override
  public String getMenuName() {
    return "Main";
  }

  @Override
  public void onInit() {
    addSteadyComponent(
        getResourceManager().getResource("main_menu_background", Sprite.class).getRawComponent(),
        0);
    MenuLabel credits = new MenuLabel(5,0,"Build " + Perceptionallity.BUILD,15,Color.WHITE);
    int[] cornerLocation = cornerLocation(credits.getDimension());
    credits.setXY(cornerLocation[0],cornerLocation[1]);
    credits.buildLabel();
    addSteadyComponent(credits.getLabel(), 1);

  }

  @Override
  public void onUpdate() {
    if (!titleInAnimation.isFinished()) {
      title =
          new MenuLabel(
              0,
                  (int) titleInAnimation.retrieveCurrentAndUpdateValue(),
              "Perceptionallity",
              50,
              Color.WHITE);
      int[] centerTitle = centerLocation(title.getDimension());
      title.setX(centerTitle[0]);
      title.buildLabel();
    } else {
      Color currentColor = title.getLabel().getForeground();
      title.getLabel().setForeground(new Color((float) currentColor.getRed() /255, (float) currentColor.getGreen() /255, (float) currentColor.getBlue() /255,
              titleGlowAnimation.retrieveCurrentAndUpdateValue()));
      if(titleGlowAnimation.isFinished()) {
        if(titleGlowAnimation.getTargetValue() == 0.3f)
          titleGlowAnimation.setTargetValue(0.9f);
        else
          titleGlowAnimation.setTargetValue(0.3f);
      }

    }
    addTempComponent(title.getLabel(), 1);
  }
}
