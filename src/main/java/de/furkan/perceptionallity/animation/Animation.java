package de.furkan.perceptionallity.animation;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;
import lombok.Getter;

@Getter
public class Animation {

  private final Sprite[] sprites;
  private final int framesPerSecond;
  private final boolean loop;
  private Iterator<Sprite> animationSprites;
  private Sprite currentFrame;

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
}
