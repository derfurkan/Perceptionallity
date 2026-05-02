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
    private final boolean loop;
    @Setter
    private int framesPerSecond;
    private Iterator<Sprite> animationSprites;
    private Sprite currentFrame;

    public Animation(Sprite[] sprites, int framesPerSecond, boolean loop) {
        this.sprites = sprites;
        if (framesPerSecond > 60) {
            //      Perceptionallity.getGame()
            //          .handleFatalException(
            //              new RuntimeException(
            //                  "Animations can only be played back with max 30 Frames per second and not
            // more."));
        }
        this.framesPerSecond = framesPerSecond;
        this.loop = loop;
        this.animationSprites = Arrays.asList(this.sprites).iterator();
    }

    public void resizeTo(Dimension dimension) {
        for (Sprite sprite : sprites) {
            sprite.resize(dimension);
        }
        //     animationSprites.forEach(sprite -> sprite.resize(dimension));
    }

    public void nextFrame() {
        if (animationSprites.hasNext()) {
            currentFrame = animationSprites.next();
            return;
        } else if (loop) {
            resetAnimation();
            return;
        }
        Perceptionallity.getGame().getLogger().warning("Animation has reached it's end.");
    }

    public void resetAnimation() {
        this.animationSprites = Arrays.asList(this.sprites).iterator();
        currentFrame = animationSprites.next();
    }

    @Override
    public Animation clone() throws CloneNotSupportedException {
        Animation cloned = (Animation) super.clone();
        cloned.animationSprites = Arrays.asList(cloned.sprites).iterator();
        cloned.currentFrame = cloned.animationSprites.next();
        return cloned;
    }
}
