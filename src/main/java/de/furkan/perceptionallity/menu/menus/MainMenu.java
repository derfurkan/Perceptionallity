package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.animation.InterpolationType;
import de.furkan.perceptionallity.animation.ValueIterator;
import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.MenuLabel;
import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;

public class MainMenu extends Menu {

  public MainMenu(int msPerUpdate) {
    super(msPerUpdate);
  }

  @Override
  public String getMenuName() {
    return "Main";
  }

  private MenuLabel title;
  private final ValueIterator titleAnimation = new ValueIterator(-90,20,10, InterpolationType.SMOOTH_END);


    @Override
  public void init() {
       addSteadyComponent(
           getResourceManager().getResource("main_menu_background",
     Sprite.class).getRawComponent(),0);

  }


  @Override
  public void update() {
    if (!titleAnimation.isFinished()) {
        Dimension titleDimension = new Dimension(590, 90);
        int[] centerTitle = centerLocation(titleDimension);
        title = new MenuLabel(centerTitle[0], titleAnimation.retrieveCurrentAndUpdateValue(), titleDimension, "Perceptionallity", 100, Color.WHITE);
        addTempComponent(title.getLabel(), 1);
    } else {
      addSteadyComponent(title.getLabel(),1);
    }
  }
}
