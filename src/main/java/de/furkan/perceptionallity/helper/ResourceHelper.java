package de.furkan.perceptionallity.helper;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.util.Sprite;
import java.awt.*;
import lombok.Getter;

@Getter
public class ResourceHelper {
    
    private final Sprite MAIN_MENU_BACKGROUND;

    public ResourceHelper() {
    MAIN_MENU_BACKGROUND =
        Perceptionallity.getSpriteBuilder()
            .buildSprite(
                new Dimension(
                    Perceptionallity.getMenuManager().getWINDOW_WIDTH(),
                    Perceptionallity.getMenuManager().getWINDOW_HEIGHT()),
                Image.SCALE_DEFAULT,"main_menu_background.png","menu","backgrounds");
    }

}
