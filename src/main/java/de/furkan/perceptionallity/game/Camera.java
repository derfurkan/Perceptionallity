package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.world.GameObject;
import java.util.HashMap;
import lombok.Getter;

public class Camera {

  @Getter private final HashMap<GameObject, int[]> calculatedGameObjects = new HashMap<>();

  private GameObject centeredObject;

  public void centerOnObject(GameObject centeredObject) {
    finishGameObject(
        centeredObject,
        Perceptionallity.getGame()
            .getMenuManager()
            .centerLocation(centeredObject.getRectangle().getSize()));
    this.centeredObject = centeredObject;
  }

  public void stopCentering() {
    calculatedGameObjects.remove(centeredObject);
    centeredObject = null;
  }

  public int[] calculateObjectPosition(GameObject gameObject) {
    int[] calculatedPosition = gameObject.getWorldLocation().getXY();
    if (centeredObject != null) {
      int diffX = centeredObject.getWorldLocation().getX() - gameObject.getWorldLocation().getX();
      int diffY = centeredObject.getWorldLocation().getY() - gameObject.getWorldLocation().getY();
      calculatedPosition =
          new int[] {
            centeredObject.getComponent().getX() + diffX,
            centeredObject.getComponent().getY() + diffY
          };
    }
    finishGameObject(gameObject, calculatedPosition);
    return calculatedPosition;
  }

  private void finishGameObject(GameObject gameObject, int[] calculatedLocation) {
    calculatedGameObjects.remove(gameObject);
    calculatedGameObjects.put(gameObject, calculatedLocation);
  }

  public void flushCalculation() {
    calculatedGameObjects.clear();
  }
}
