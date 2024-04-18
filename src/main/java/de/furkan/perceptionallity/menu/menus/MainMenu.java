package de.furkan.perceptionallity.menu.menus;


import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.menu.Menu;

public class MainMenu extends Menu {

    @Override
    public void init() {

        addComponent(Perceptionallity.getResourceHelper().getMAIN_MENU_BACKGROUND().getRawComponent());


    }
}
