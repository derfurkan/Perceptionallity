package de.furkan.perceptionallity.game.entity;

import de.furkan.perceptionallity.game.GameObject;
import de.furkan.perceptionallity.game.WorldLocation;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.HashMap;

@Setter
public class GameEntity extends GameObject {
    @Getter
    private GameEntity gameEntityObject;
    private HashMap<EntityAttributes, Integer> entityAttributes = new HashMap<>();

    public GameEntity(
            Dimension dimension, WorldLocation worldLocation, boolean passToCollisionCheck) {
        super(dimension, worldLocation, passToCollisionCheck);
        this.gameEntityObject = this;
    }

    public void setAttribute(EntityAttributes entityAttribute, int value) {
        entityAttributes.remove(entityAttribute);
        entityAttributes.put(entityAttribute, value);
    }

    public int getAttribute(EntityAttributes entityAttribute) {
        if (entityAttributes.containsKey(entityAttribute)) {
            return entityAttributes.get(entityAttribute);
        }
        return 0;
    }
}
