package de.furkan.perceptionallity.game.entity.npc;

import de.furkan.perceptionallity.animation.Animation;
import de.furkan.perceptionallity.game.WorldLocation;
import java.awt.*;

public class TestNPC extends GameNPC {

  public TestNPC(WorldLocation worldLocation) throws Exception {
    super(new Dimension(100, 110), worldLocation);
    playAnimation(
        getResourceManager().getResource("player_idle_down_animation", Animation.class), 1, true);
  }
}
