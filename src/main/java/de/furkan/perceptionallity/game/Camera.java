package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import java.util.HashMap;
import lombok.Getter;

public class Camera {

  @Getter private final HashMap<GameObject, int[]> calculatedGameObjects = new HashMap<>();

  private GameObject centeredObject;

  /**
   * Centers the camera on a specified game object.
   *
   * @param centeredObject The game object to center the camera on.
   */
  public void centerOnObject(GameObject centeredObject) {
    this.centeredObject = centeredObject;
  }

  /**
   * Stops the camera from centering on the current object and removes it from the tracking list.
   */
  public void stopCentering() {
    calculatedGameObjects.remove(centeredObject);
    centeredObject = null;
  }

  /**
   * Calculates the position of a game object relative to the centered object. If there is no
   * centered object, it calculates the position based on the game object's own location.
   *
   * @param gameObject The game object for which to calculate the position.
   * @return An array of two integers representing the x and y coordinates.
   */
  public int[] calculateObjectPosition(GameObject gameObject) {
    int[] calculatedPosition = gameObject.getWorldLocation().getXY();

    if (centeredObject != null && centeredObject != gameObject) {
      int diffX = gameObject.getWorldLocation().getX() - centeredObject.getWorldLocation().getX();
      int diffY = gameObject.getWorldLocation().getY() - centeredObject.getWorldLocation().getY();

      int offsetX = centeredObject.getComponent().getWidth() / 2;
      int offsetY = centeredObject.getComponent().getHeight() / 2;

      calculatedPosition =
          new int[] {
            centeredObject.getComponent().getX() + diffX - offsetX,
            centeredObject.getComponent().getY() + diffY - offsetY
          };
    } else if (centeredObject == gameObject) {
      calculatedPosition =
          Perceptionallity.getGame()
              .getMenuManager()
              .centerLocation(gameObject.getRectangle().getSize());
    }

    finishGameObject(gameObject, calculatedPosition);
    return calculatedPosition;
  }

  /**
   * Updates the stored location of a game object after recalculating its position.
   *
   * @param gameObject The game object whose position has been recalculated.
   * @param calculatedLocation The new calculated position of the game object.
   */
  private void finishGameObject(GameObject gameObject, int[] calculatedLocation) {
    calculatedGameObjects.remove(gameObject);
    calculatedGameObjects.put(gameObject, calculatedLocation);
  }

  /** Clears all stored calculations of game object positions. */
  public void flushCalculation() {
    calculatedGameObjects.clear();
  }
}
