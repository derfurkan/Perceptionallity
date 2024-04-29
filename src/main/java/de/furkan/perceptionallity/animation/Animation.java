package de.furkan.perceptionallity.animation;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.GameObject;
import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;
import lombok.Getter;

@Getter
public class Animation {

    private final Sprite[] sprites;
    private Iterator<Sprite> animationSprites;
    private Sprite currentFrame;
    private final int  framesPerSecond;
    private final boolean loop;

    public Animation(Sprite[] sprites, int framesPerSecond, boolean loop) {
        this.sprites = sprites;
        this.framesPerSecond = framesPerSecond;
        this.loop = loop;
        this.currentFrame = sprites[0];
        this.animationSprites = Arrays.asList(this.sprites).iterator();
    }
    
    public void resizeTo(Dimension dimension) {
        animationSprites.forEachRemaining(sprite -> sprite.resize(dimension));
    }

    public void start(GameObject gameObject) {
        if(Perceptionallity.getGame().getGameManager().getPlayingAnimations().containsKey(gameObject)) {
            Perceptionallity.getGame().getLogger().warning("GameObject is already being animated so it has been stopped. In future please stop the current animation first when starting a new one.");
            stop();
        }
        Perceptionallity.getGame().getGameManager().getPlayingAnimations().put(gameObject,this);
    }

    public void stop() {
        Perceptionallity.getGame().getGameManager().getPlayingAnimations().remove(this);
    }

    public void nextFrame() {
        if (animationSprites.hasNext()) {
            currentFrame = animationSprites.next();
            return;
        } else if (loop) {
            resetAnimation();
            currentFrame =  animationSprites.next();
            return;
        }
        Perceptionallity.getGame().getLogger().warning("Animation has reached it's end.");
    }

    private void resetAnimation() {
        this.animationSprites = Arrays.asList(this.sprites).iterator();
    }

}
