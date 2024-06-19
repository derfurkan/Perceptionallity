package de.furkan.perceptionallity.game;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import java.util.HashMap;
import lombok.Getter;

public class Camera {
  @Getter
  private final HashMap<GameObject, int[]> calculatedGameObjects = new HashMap<>();
  @Getter
  private GameObject centeredObject;
  
  private final WorldLocation cameraViewLocation = new WorldLocation(0,0);
  private final WorldLocation centeredCameraViewLocation = new WorldLocation(0,0);


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
    int[] calculatedPosition =  new int[] {
            gameObject.getWorldLocation().getX() + cameraViewLocation.getX(),
            gameObject.getWorldLocation().getY() + cameraViewLocation.getY()
    };

    if (centeredObject != null && centeredObject != gameObject) {
      int diffX = gameObject.getWorldLocation().getX() - centeredObject.getWorldLocation().getX();
      int diffY = gameObject.getWorldLocation().getY() - centeredObject.getWorldLocation().getY();

      calculatedPosition =
          new int[] {
            centeredObject.getComponent().getX() + diffX,
            centeredObject.getComponent().getY() + diffY
          };
    } else if (centeredObject == gameObject) {
      calculatedPosition =
          Perceptionallity.getGame().getMenuManager().centerLocation(gameObject.getDimension());

      centeredCameraViewLocation.setXY((centeredObject.getWorldLocation().getX() + centeredObject.getDimension().width/2) - (Perceptionallity.getGame().getWINDOW_WIDTH()/2), (centeredObject.getWorldLocation().getY()  + centeredObject.getDimension().height/2) - (Perceptionallity.getGame().getWINDOW_HEIGHT()/2));

    }

    return calculatedPosition;
  }

  public int[] calculateObjectPosition(int x, int y) {
    int[] calculatedPosition = new int[] {x, y};

    if (centeredObject != null) {
      int diffX = x - centeredObject.getWorldLocation().getX();
      int diffY = y - centeredObject.getWorldLocation().getY();

      int offsetX = centeredObject.getComponent().getWidth() / 2;
      int offsetY = centeredObject.getComponent().getHeight() / 2;

      calculatedPosition =
          new int[] {
            centeredObject.getComponent().getX() + diffX - offsetX,
            centeredObject.getComponent().getY() + diffY - offsetY
          };
    }

    return calculatedPosition;
  }

  public WorldLocation getCameraViewLocation() {
    return centeredObject != null ? centeredCameraViewLocation : cameraViewLocation;
  }

  /**
   * Updates the stored location of a game object after recalculating its position.
   *
   * @param gameObject The game object whose position has been recalculated.
   * @param calculatedLocation The new calculated position of the game object.
   */
  public void finishGameObject(GameObject gameObject, int[] calculatedLocation) {
    calculatedGameObjects.remove(gameObject);
    calculatedGameObjects.put(gameObject, calculatedLocation);
  }

  /** Clears all stored calculations of game object positions. */
  public void flushCalculation() {
    calculatedGameObjects.clear();
  }

  public boolean isCenteredObject(GameObject gameObject) {
    if (centeredObject == null) return false;
    else return centeredObject == gameObject;
  }
}
