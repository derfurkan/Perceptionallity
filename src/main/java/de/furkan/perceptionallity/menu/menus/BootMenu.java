package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.animation.InterpolationType;
import de.furkan.perceptionallity.animation.ValueIterator;
import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.label.MenuLabel;
import de.furkan.perceptionallity.menu.components.sprite.MenuSprite;
import java.awt.*;

public class BootMenu extends Menu {

  private final ValueIterator engineLogoFadeIn =
      new ValueIterator(0, 1, .01f, InterpolationType.DEFAULT);
  private MenuSprite engineLogoSprite;
  private MenuLabel engineTitleLabel;

  public BootMenu() {
    super(30, Color.BLACK);
  }

  @Override
  public String getMenuName() {
    return "Boot";
  }

  @Override
  public void initComponents() {
    engineLogoSprite = new MenuSprite(0, 0, new Dimension(550, 600), null);
    Animation spriteAnimation =
        getResourceManager().getResource("engine_animation_logo", Animation.class);
    spriteAnimation.resizeTo(engineLogoSprite.getDimension());
    engineLogoSprite.setSpriteAnimation(spriteAnimation);

    int[] centeredLocation = getMenuManager().centerLocation(engineLogoSprite.getDimension());
    engineLogoSprite.setXY(centeredLocation[0], centeredLocation[1] - 150);

    engineLogoSprite.buildComponent();

    engineTitleLabel = new MenuLabel(0, 0, "ENGINE NAME HIER, NIGGA", 80, Color.WHITE);

    centeredLocation = getMenuManager().centerLocation(engineTitleLabel.getDimension());
    engineTitleLabel.setX(centeredLocation[0]);
    engineTitleLabel.setBelow(engineLogoSprite, 0);

    engineTitleLabel.buildComponent();
  }

  @Override
  public void onUpdate() {

    if (engineLogoSprite.getSpriteAnimation() != null) {

      if ((getUpdates()
              % ((1000 / getMsPerUpdate())
                  / engineLogoSprite.getSpriteAnimation().getFramesPerSecond())
          == 0)) {
        engineLogoSprite.displayNextAnimationFrame();
      }
    }
    if (engineLogoSprite.getSprite() != null) {
      if (engineLogoFadeIn.isFinished()) {
        addSteadyComponent(engineLogoSprite.getJComponent(), 1);
        addSteadyComponent(engineTitleLabel.getJComponent(), 1);
        if (getSecondsElapsed() == 7) {
          engineLogoFadeIn.setStep(.025f);
          engineLogoFadeIn.reverse();

        } else if (getSecondsElapsed() == 8) {
          getMenuManager().setCurrentMenu(new StartMenu());
          getMenuManager().drawCurrentMenu();
        }
      } else {
        engineLogoSprite.setOpacity(engineLogoFadeIn.getCurrentValue());
        engineTitleLabel.setOpacity(engineLogoFadeIn.getCurrentValue());
        addTempComponent(engineLogoSprite.getJComponent(), 1);
        addTempComponent(engineTitleLabel.getJComponent(), 1);
        engineLogoFadeIn.updateValue();
      }
    }
  }
}
