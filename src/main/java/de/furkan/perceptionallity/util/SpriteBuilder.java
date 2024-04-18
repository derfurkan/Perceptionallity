package de.furkan.perceptionallity.util;

import java.awt.*;
import javax.swing.*;

public class SpriteBuilder {

    public Sprite buildSprite(Dimension dimension, int scaling, String resourceKey, String ...resourcePath) {
        return new Sprite(getClass().getResource("/"+String.join("/",resourcePath)+"/"+resourceKey),dimension,scaling);
    }

}

