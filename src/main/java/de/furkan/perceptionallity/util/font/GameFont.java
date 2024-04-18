package de.furkan.perceptionallity.util.font;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import lombok.Getter;

@Getter
public class GameFont {

  private final Font font;

  public GameFont(int fontType, String fontKey, String... fontPath) {
    try {
      this.font =
          Font.createFont(
              fontType, Perceptionallity.getResourceManager().getResourceFile(fontKey, fontPath));
    } catch (Exception e) {
      throw new RuntimeException("Failed to load font: " + e.getMessage());
    }
  }
}
