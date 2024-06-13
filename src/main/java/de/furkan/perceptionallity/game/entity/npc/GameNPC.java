package de.furkan.perceptionallity.game.entity.npc;

import de.furkan.perceptionallity.game.GameSprite;
import de.furkan.perceptionallity.game.WorldLocation;
import de.furkan.perceptionallity.game.entity.GameEntity;
import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;

public class GameNPC extends GameEntity {

  private final GameSprite interactArrow;

  public GameNPC(Dimension dimension, WorldLocation worldLocation) throws Exception {
    super(dimension, worldLocation, false);
    Sprite sprite = getResourceManager().getResource("npc_interact_arrow", Sprite.class);
    interactArrow = new GameSprite(new Dimension(50, 50), new WorldLocation(), sprite);
    interactArrow
        .getWorldLocation()
        .setXY(
            worldLocation.getX() + interactArrow.getDimension().width / 2,
            (worldLocation.getY() - interactArrow.getDimension().height / 2) - 10);
    interactArrow.initializeGameObject(1);
    interactArrow.setOpacity(0.0f);
  }

  public void showInteractArrow() {
    interactArrow.setOpacity(1.0f);
  }

  public void hideInteractArrow() {
    interactArrow.setOpacity(0.0f);
  }

  public boolean isInteractionArrowShown() {
    return interactArrow.getOpacity() == 1.0f;
  }
}
