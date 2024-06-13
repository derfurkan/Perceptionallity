package de.furkan.perceptionallity.util.font;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import java.io.IOException;
import lombok.Getter;

@Getter
public class GameFont {

  private final Font font;

  /**
   * Constructs a GameFont object by loading a font from the specified file path. The font is loaded
   * using the Font.createFont method, which throws an exception if the font cannot be loaded.
   *
   * @param fontType the type of the font (e.g., Font.TRUETYPE_FONT)
   * @param fontKey the key used to identify the font file in the resource manager
   * @param fontPath additional path components leading to the font file
   * @throws RuntimeException if the font cannot be loaded, encapsulating the original exception
   */
  public GameFont(int fontType, String fontKey, String... fontPath)
      throws IOException, FontFormatException {
    this.font =
        Font.createFont(
            fontType,
            Perceptionallity.getGame().getResourceManager().getResourceFile(fontKey, fontPath));
  }
}
