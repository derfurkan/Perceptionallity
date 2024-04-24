package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.game.world.GameObject;
import java.util.HashMap;
import lombok.Getter;

public class Camera {

  @Getter private final HashMap<GameObject, int[]> calculatedGameObjects = new HashMap<>();

  private GameObject centeredObject;

  public void centerOnObject(GameObject centeredObject) {
    this.centeredObject = centeredObject;
  }

  public void stopCentering() {
    calculatedGameObjects.remove(centeredObject);
    centeredObject = null;
  }

  public int[] calculateObjectPosition(GameObject gameObject) {
    int[] calculatedPosition = gameObject.getWorldLocation().getXY();
    if (centeredObject != null && centeredObject != gameObject) {
      int diffX = gameObject.getWorldLocation().getX() - centeredObject.getWorldLocation().getX();
      int diffY = gameObject.getWorldLocation().getY() - centeredObject.getWorldLocation().getY();

      int offsetX = centeredObject.getComponent().getWidth() / 2;
      int offsetY = centeredObject.getComponent().getHeight() / 2;
      calculatedPosition = new int[]{
              centeredObject.getComponent().getX() + diffX - offsetX,
              centeredObject.getComponent().getY() + diffY - offsetY
      };
    } else if (centeredObject == gameObject) {
      calculatedPosition = Perceptionallity.getGame().getMenuManager().centerLocation(gameObject.getRectangle().getSize());
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
