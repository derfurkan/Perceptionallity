package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.util.sprite.Sprite;

import javax.swing.*;
import java.awt.*;

public class MenuSprite extends MenuComponent {

    private final Sprite sprite;

    @Override
    public JComponent getJComponent() {
        return sprite.getRawComponent();
    }

    public MenuSprite(Sprite sprite) {
        super(sprite.getRawComponent().getX(), sprite.getRawComponent().getY(), new Dimension(sprite.getRawComponent().getWidth(),sprite.getRawComponent().getHeight()));
        this.sprite = sprite;
    }



}
