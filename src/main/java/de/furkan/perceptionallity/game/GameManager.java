package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Manager;
import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.entity.player.GamePlayer;
import de.furkan.perceptionallity.game.world.GameObject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;

import javax.swing.*;

@Getter
public class GameManager extends Manager {

  private final Camera camera = new Camera();

  private final HashMap<Component, GameObject> registeredGameObjects = new HashMap<>();

  public GameManager() {}

  @Override
  public void initialize() {

    GamePlayer gamePlayer = new GamePlayer(new WorldLocation(20,20),"initial_player");

    GamePlayer gamePlayer1 = new GamePlayer(new WorldLocation(100,100),"game_icon");
    gamePlayer.buildGameObject();
    gamePlayer1.buildGameObject();
    Timer timer =
        new Timer(
            100,
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                gamePlayer.getWorldLocation().setX(ThreadLocalRandom.current().nextInt(-100,200));
                gamePlayer.getWorldLocation().setY(ThreadLocalRandom.current().nextInt(-100,200));
                Perceptionallity.getGame().getGamePanel().repaint();
                Perceptionallity.getGame().getGamePanel().revalidate();
              }
            });
    timer.start();


    camera.stopCentering();
    camera.centerOnObject(gamePlayer);


  }

  public boolean isGameComponent(Component component) {
    return registeredGameObjects.containsKey(component);
  }

  public void registerGameObject(GameObject gameObject) {
    registeredGameObjects.put(gameObject.getComponent(),gameObject);
  }

  public void unregisterGameObject(GameObject gameObject) {
    registeredGameObjects.remove(gameObject.getComponent());
  }

}
