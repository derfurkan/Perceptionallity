package de.furkan.perceptionallity.game.world;

import de.furkan.perceptionallity.game.WorldLocation;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class GameObject {

  private WorldLocation worldLocation;
  private Rectangle rectangle;
  private JComponent component;
}
