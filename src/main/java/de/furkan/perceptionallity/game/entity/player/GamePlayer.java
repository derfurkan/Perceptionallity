package de.furkan.perceptionallity.game.entity.player;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.WorldLocation;
import de.furkan.perceptionallity.game.entity.GameEntity;
import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;

public class GamePlayer extends GameEntity {

  public GamePlayer(WorldLocation worldLocation,String textureName) {
    super(
        new Rectangle(worldLocation.getX(), worldLocation.getY(), 50, 80),
        worldLocation,
        Perceptionallity.getGame().getResourceManager().getResource(textureName, Sprite.class),
        100,
        70,"Player");


    //TODO: Create controller and set it here
    //TODO: Create animator and set it here


  }
}
