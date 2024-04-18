package de.furkan.perceptionallity.util;

import java.awt.*;
import javax.swing.*;

public class SpriteBuilder {

  public Sprite buildSprite(
      Dimension dimension, int scaling, String spriteKey, String... spritePath) {
    return new Sprite(
        getClass().getResource("/" + String.join("/", spritePath) + "/" + spriteKey),
        dimension,
        scaling);
  }
}
