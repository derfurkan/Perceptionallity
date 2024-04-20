package de.furkan.perceptionallity.util.sprite;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;

public class SpriteBuilder {

  public Sprite buildSprite(Dimension dimension, String spriteKey, String... spritePath) {
    return new Sprite(
        Perceptionallity.getGame()
            .getResourceManager()
            .getResourceFile(spriteKey, spritePath), // TODO: Add getResourceManager method
        dimension);
  }
}
