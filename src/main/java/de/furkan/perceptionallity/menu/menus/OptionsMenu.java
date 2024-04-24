package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.animation.InterpolationType;
import de.furkan.perceptionallity.animation.ValueIterator;
import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.*;
import java.awt.*;
import javax.swing.*;

public class OptionsMenu extends Menu {
  final ValueIterator optionsLabelAnimation, backButtonFadeAnimation;
  MenuLabel optionsLabel = new MenuLabel(0, 10, "OPTIONS", 50, Color.WHITE);
  MenuButton backButton = new MenuButton(20, 0, 50, "BACK");

  public OptionsMenu() {
    super(30, Color.BLACK);
    optionsLabelAnimation =
        new ValueIterator(
            (float) -optionsLabel.getDimension().getWidth(), 20, 10, InterpolationType.SMOOTH_END);
    backButtonFadeAnimation = new ValueIterator(0.0f, 1.0f, 0.03f, InterpolationType.DEFAULT);
  }

  @Override
  public String getMenuName() {
    return "Options";
  }

  @Override
  public void initComponents() {

    int[] edgeLocation = getMenuManager().edgeLocation(backButton.getDimension());
    backButton.setY(edgeLocation[1]);

    MenuSlider menuSlider = new MenuSlider(20, 80, new Dimension(200, 50), 10, 0, 100, 50);
    menuSlider.setMenuSliderChangeEvent(
        new MenuSliderChangeEvent() {
          @Override
          public void onChange(JSlider jSlider) {
            Perceptionallity.getGame()
                .getSoundEngine()
                .setVolumeOfAll((float) jSlider.getValue() / 100);
          }
        });
    menuSlider.buildComponent();
    addSteadyComponent(menuSlider.getJComponent(), 1);
  }

  @Override
  public void onUpdate() {

    if (!optionsLabelAnimation.isFinished()) {
      optionsLabel.setX((int) optionsLabelAnimation.getCurrentValue());
      optionsLabel.buildComponent();
      optionsLabelAnimation.updateValue();
      addTempComponent(optionsLabel.getJComponent(), 1);
    } else {
      addSteadyComponent(optionsLabel.getJComponent(), 1);

      if (!backButtonFadeAnimation.isFinished()) {
        backButton.setAlpha(backButtonFadeAnimation.getCurrentValue());
        backButtonFadeAnimation.updateValue();
        backButton.buildComponent();
        addTempComponent(backButton.getJComponent(), 1);
      } else {
        addSteadyComponent(backButton.getJComponent(), 1);
        backButton.setButtonClick(
            () -> {
              Perceptionallity.getGame().getMenuManager().setCurrentMenu(new MainMenu());
              Perceptionallity.getGame().getMenuManager().drawCurrentMenu();
            });
      }
    }
  }
}
