package de.furkan.perceptionallity;

import de.furkan.perceptionallity.helper.ResourceHelper;
import de.furkan.perceptionallity.menu.menus.MainMenu;
import de.furkan.perceptionallity.menu.MenuManager;
import de.furkan.perceptionallity.util.SpriteBuilder;
import lombok.Getter;

public class Perceptionallity {

    @Getter
    private static MenuManager menuManager;

    @Getter
    private static ResourceHelper resourceHelper;

    @Getter
    private static SpriteBuilder spriteBuilder;

    //In case we need this for later.
    @Getter
    private static MainMenu mainMenu;

    public static void main(String[] args) {
        menuManager = new MenuManager();
        spriteBuilder = new SpriteBuilder();
        resourceHelper = new ResourceHelper();


        menuManager.setCurrentMenu(mainMenu = new MainMenu());
        menuManager.drawCurrentMenu();
    }

}
