package de.furkan.perceptionallity.animation;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.util.sprite.Sprite;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;

@Getter
public class Animation implements Cloneable {

    private final Sprite[] sprites;
    @Setter
    private int framesPerSecond;
    private final boolean loop;
    private Iterator<Sprite> animationSprites;
    private Sprite currentFrame;

    public Animation(Sprite[] sprites, int framesPerSecond, boolean loop) {
        this.sprites = sprites;
        if(framesPerSecond > 30) {
            throw new RuntimeException("Animations can only be played back with max 30 Frames per second and not more.");
        }
        this.framesPerSecond = framesPerSecond;
        this.loop = loop;
        this.currentFrame = sprites[0];
        this.animationSprites = Arrays.asList(this.sprites).iterator();
    }

    public void resizeTo(Dimension dimension) {
        animationSprites.forEachRemaining(sprite -> sprite.resize(dimension));
    }

    public void nextFrame() {
        if (animationSprites.hasNext()) {
            currentFrame = animationSprites.next();
            return;
        } else if (loop) {
            resetAnimation();
            currentFrame = animationSprites.next();
            return;
        }
        Perceptionallity.getGame().getLogger().warning("Animation has reached it's end.");
    }

    private void resetAnimation() {
        this.animationSprites = Arrays.asList(this.sprites).iterator();
    }

    @Override
    public Animation clone() {
        try {
            return (Animation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
