package de.furkan.perceptionallity.menu.menus;

import de.furkan.perceptionallity.menu.Menu;
import de.furkan.perceptionallity.menu.components.label.MenuLabel;
import java.awt.*;

public class LoadingMenu extends Menu {

    public LoadingMenu() {
        super(-1,Color.BLACK);
    }

    @Override
    public String getMenuName() {
        return "Loading";
    }

    @Override
    public void initComponents() throws Exception {
        MenuLabel loadingLabel = new MenuLabel(0,0,"LOADING",120,Color.WHITE);
    int[] centerLocation =
        getMenuManager().centerLocation(loadingLabel.getDimension());

    loadingLabel.setXY(centerLocation[0],centerLocation[1]);

    loadingLabel.buildComponent();
    addSteadyComponent(loadingLabel.getJComponent(),1);

    }

    @Override
    public void onUpdate() throws Exception {

    }
}
