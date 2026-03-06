package de.furkan.perceptionallity.game.entity.environment;

import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.game.GameObject;
import de.furkan.perceptionallity.game.WorldLocation;
import de.furkan.perceptionallity.game.lighting.LightSource;

import java.awt.*;

public class GameCampfire extends GameObject {

    public GameCampfire(WorldLocation worldLocation) {
        super(new Dimension(80, 80), worldLocation, true);
        playAnimation(getResourceManager().getResource("campfire_animation", Animation.class), 15);
        setCollisionBoundaries(new Dimension(50, 70));
        setLightSource(new LightSource(worldLocation, 180, new Color(255, 180, 80), 0.9f));
        getLightSource().setFlickering(true);
        getLightSource().setFlickerAmplitude(3);
    }
}
