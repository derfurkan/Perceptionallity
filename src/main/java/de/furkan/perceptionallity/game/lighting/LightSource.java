package de.furkan.perceptionallity.game.lighting;

import de.furkan.perceptionallity.game.Camera;
import de.furkan.perceptionallity.game.GameObject;
import de.furkan.perceptionallity.game.WorldLocation;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class LightSource {

    private final WorldLocation worldLocation;
    private final int baseRadius;
    private final Color color;
    private final float intensity;
    private GameObject parentObject;
    private boolean flickering;
    private int flickerAmplitude;
    private int flickerOffset;

    public LightSource(WorldLocation worldLocation, int radius, Color color, float intensity) {
        this.worldLocation = worldLocation;
        this.baseRadius = radius;
        this.color = color;
        this.intensity = Math.max(0f, Math.min(1f, intensity));
    }

    public int getRadius() {
        return baseRadius + flickerOffset;
    }

    public void updateFlicker() {
        if (flickering) {
            flickerOffset = ThreadLocalRandom.current().nextInt(-flickerAmplitude, flickerAmplitude + 1);
        }
    }

    public int[] getScreenPosition(Camera camera) {
        if (parentObject != null && parentObject.getComponent() != null) {
            Rectangle bounds = parentObject.getComponent().getBounds();
            return new int[]{
                    bounds.x + bounds.width / 2,
                    bounds.y + bounds.height / 2
            };
        }
        return camera.calculateObjectPosition(worldLocation.getX(), worldLocation.getY());
    }
}
