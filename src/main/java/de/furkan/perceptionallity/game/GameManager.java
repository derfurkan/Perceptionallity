package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.game.world.GameObject;
import java.awt.*;
import java.util.HashMap;
import lombok.Getter;

@Getter
public class GameManager extends Manager {

  private final Camera camera = new Camera();

  private final HashMap<Component, GameObject> registeredGameObjects = new HashMap<>();

  public GameManager() {}

  @Override
  public void initialize() {



  }

  public boolean isGameComponent(Component component) {
    return registeredGameObjects.containsKey(component);
  }
}
