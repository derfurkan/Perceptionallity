package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.button.MenuButton;
import de.furkan.perceptionallity.menu.components.button.MenuButtonClick;
import de.furkan.perceptionallity.menu.components.checkbox.MenuCheckbox;
import de.furkan.perceptionallity.menu.components.label.MenuLabel;
import java.awt.*;

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
    MenuLabel optionsLabel = new MenuLabel(20, 10, "OPTIONS", 120, Color.WHITE);

    MenuLabel multiThreadedRenderingLabel = new MenuLabel(30, 0, "Multi-Threaded Rendering", 50, Color.WHITE);
    multiThreadedRenderingLabel.setBelow(optionsLabel,30);

    MenuCheckbox multiThreadedRenderingCheckbox = new MenuCheckbox(0,0,50,5);
    multiThreadedRenderingCheckbox.setAsideRight(multiThreadedRenderingLabel,5);
    multiThreadedRenderingCheckbox.setCenteredHeight(multiThreadedRenderingLabel);


    MenuLabel discordRPCLabel = new MenuLabel(30, 0, "Discord Integration", 50, Color.WHITE);
    discordRPCLabel.setBelow(multiThreadedRenderingLabel,10);

    MenuCheckbox discordRPCCheckbox = new MenuCheckbox(0,0,50,5);
    discordRPCCheckbox.setAsideRight(discordRPCLabel,5);
    discordRPCCheckbox.setCenteredHeight(discordRPCLabel);

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

    backButton.buildComponent();
    optionsLabel.buildComponent();

    multiThreadedRenderingLabel.buildComponent();
    multiThreadedRenderingCheckbox.buildComponent();

    discordRPCLabel.buildComponent();
    discordRPCCheckbox.buildComponent();

    addSteadyComponent(backButton.getJComponent(), 1);
    addSteadyComponent(multiThreadedRenderingLabel.getJComponent(), 1);
    addSteadyComponent(multiThreadedRenderingCheckbox.getJComponent(), 1);
    addSteadyComponent(discordRPCLabel.getJComponent(), 1);
    addSteadyComponent(discordRPCCheckbox.getJComponent(), 1);
    addSteadyComponent(optionsLabel.getJComponent(), 1);
  }

  @Override
  public void onUpdate() {}
}
