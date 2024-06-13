package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.*;
import java.awt.*;
import javax.swing.*;

public class OptionsMenu extends Menu {

  public OptionsMenu() {
    super(-1, Color.BLACK);
  }

  @Override
  public String getMenuName() {
    return "Options";
  }

  @Override
  public void initComponents() throws Exception {
    MenuLabel optionsLabel = new MenuLabel(20, 10, "OPTIONS", 50, Color.WHITE);
    MenuLabel fpsSetting = new MenuLabel(30, 20, "FPS Limit", 30, Color.WHITE);
    MenuLabel fpsSettingValue = new MenuLabel(0, 0, "60", 30, Color.WHITE);
    MenuSlider menuSlider = new MenuSlider(30, 0, new Dimension(200, 50), 10, 30, 160, 60);
    fpsSetting.setBelow(optionsLabel, 0);

    menuSlider.setBelow(fpsSetting, 0);

    fpsSettingValue.setAsideRight(menuSlider);
    fpsSettingValue.setSameHeight(menuSlider);

    MenuButton backButton = new MenuButton(20, 0, 50, "BACK");

    backButton.setButtonClick(
        new MenuButtonClick() {
          @Override
          public void onClick() throws Exception {
            getMenuManager().setCurrentMenu(new MainMenu());
            getMenuManager().drawCurrentMenu();
          }
        });

    int[] edgeLocation = getMenuManager().edgeLocation(backButton.getDimension());
    backButton.setY(edgeLocation[1]);

    menuSlider.setMenuSliderChangeEvent(
        new MenuSliderChangeEvent() {
          @Override
          public void onChange(JSlider jSlider) {
            fpsSettingValue.setText(String.valueOf(jSlider.getValue()));
            fpsSettingValue.recalculateDimension();
            fpsSettingValue.buildComponent();
          }
        });

    menuSlider.buildComponent();
    fpsSettingValue.buildComponent();
    backButton.buildComponent();
    optionsLabel.buildComponent();

    fpsSetting.buildComponent();
    addSteadyComponent(backButton.getJComponent(), 1);
    addSteadyComponent(fpsSettingValue.getJComponent(), 1);
    addSteadyComponent(fpsSetting.getJComponent(), 1);
    addSteadyComponent(optionsLabel.getJComponent(), 1);
    addSteadyComponent(menuSlider.getJComponent(), 1);
  }

  @Override
  public void onUpdate() {}
}
