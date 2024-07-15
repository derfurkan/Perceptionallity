package de.furkan.perceptionallity.resources;

import de.furkan.perceptionallity.Perceptionallity;
import de.furkan.perceptionallity.util.sprite.Sprite;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import lombok.Getter;

public class ResourceManager {

  @Getter
  private final ConcurrentHashMap<String, Resource<?>> resources = new ConcurrentHashMap<>();

  private final ConcurrentHashMap<String, File> resourceFileCache = new ConcurrentHashMap<>();

  private Logger getLogger() {
    return Perceptionallity.getGame().getLogger();
  }

  /**
   * Registers a new resource with a specified key and value in the resource map. Logs the action of
   * resource loading with the resource key and type. If the resource key is already in use, an
   * IllegalArgumentException is thrown.
   *
   * @param resourceKey the unique key to identify the resource
   * @param resourceValue the resource object to be stored
   * @param <T> the type of the resource
   * @throws IllegalArgumentException if the resource key is already registered
   */
  public <T> void registerResource(String resourceKey, T resourceValue) {
    Perceptionallity.getGame()
        .getLogger()
        .info(
            "Loading resource: "
                + resourceKey
                + " ("
                + resourceValue.getClass().getSimpleName()
                + ")");
    if (resources.containsKey(resourceKey)) {
      getLogger().warning("Resource key already registered. (" + resourceKey + ")");
      return;
    }
    resources.put(resourceKey, new Resource<>(resourceValue));
  }

  /**
   * Retrieves a resource by its key, ensuring the resource matches the expected type. This method
   * returns a clone of the resource to prevent modifications to the original resource. If the
   * resource key is invalid or the resource is not loaded, an IllegalArgumentException is thrown.
   *
   * @param resourceKey the key of the resource to retrieve
   * @param type the expected class type of the resource
   * @param <T> the type parameter of the resource
   * @return a clone of the resource of type T
   * @throws IllegalArgumentException if the resource key is invalid or the resource is not loaded
   */
  @SuppressWarnings("unchecked")
  public <T> T getResource(String resourceKey, Class<T> type) {
    Resource<?> resource = resources.get(resourceKey);
    if (!type.isInstance(resource.data())) {
      Perceptionallity.handleFatalException(
          "The given resource does to correspond to the provided type.");
      return null;
    }
    try {
      return (T) resource.clone().data();
    } catch (Exception e) {
      Perceptionallity.handleFatalException(e);
    }
    return null;
  }

  public String getKeyFromResource(Object resource) {
    if (resources.values().stream().anyMatch(resource1 -> resource1 == resource)) {
      return resources.keySet().stream()
          .filter(resource1 -> resources.get(resource1) == resource)
          .findFirst()
          .get();
    }
    getLogger()
        .warning(
            "Could not trace resource ("
                + resource.getClass().getSimpleName()
                + ") back to it's position in resource register. (Invalid registration?");
    return "";
  }

  /**
   * Retrieves a File object for a resource based on a resource key and path. If the resource file
   * is already cached, it returns the cached file. If not, it attempts to load the file from the
   * specified path and caches it if found. Throws a RuntimeException if the resource file cannot be
   * found or the path is invalid.
   *
   * @param resourceKey the key of the resource file to retrieve
   * @param resourcePath the path components leading to the resource file
   * @return the File object for the requested resource
   * @throws RuntimeException if the resource file or path is invalid
   */
  public File getResourceFile(String resourceKey, String... resourcePath) {
    String resourcePathString = String.join("/", resourcePath);
    String fullPath = resourcePathString + "/" + resourceKey;

    if (resourceFileCache.containsKey(fullPath)) {
      return resourceFileCache.get(fullPath);
    }

    Optional<URL> resource = Optional.ofNullable(getClass().getResource("/" + fullPath));
    if (resource.isEmpty()) {
      Perceptionallity.handleFatalException(
          "Could not retrieve resource file. (" + resourceKey + " -> " + resourcePathString + ")");
    }

    File tempFile;
    try (InputStream inputStream = resource.get().openStream()) {
      tempFile = File.createTempFile("resource-", ".tmp");
      tempFile.deleteOnExit();
      Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      Perceptionallity.handleFatalException(e);
      return null;
    }
    resourceFileCache.put(fullPath, tempFile);
    return tempFile;
  }

  // Idk where to put this
  public Sprite[] cutSpriteSheet(Sprite spriteSheet, int rows, int columns) {
    if (spriteSheet.getRawImage() instanceof BufferedImage bufferedSheet) {

      int spriteWidth = bufferedSheet.getWidth() / columns;
      int spriteHeight = bufferedSheet.getHeight() / rows;
      Sprite[] sprites = new Sprite[rows * columns];
      int index = 0;
      for (int y = 0; y < rows; y++) {
        for (int x = 0; x < columns; x++) {
          int xCord = x * spriteWidth;
          int yCord = y * spriteHeight;
          BufferedImage subImage =
              bufferedSheet.getSubimage(xCord, yCord, spriteWidth, spriteHeight);
          sprites[index++] =
              new Sprite(subImage, new Dimension(subImage.getWidth(), subImage.getHeight()));
        }
      }
      return sprites;
    } else {
      Perceptionallity.handleFatalException("Sprite image is not a BufferedImage.");
      return null;
    }
  }
}
