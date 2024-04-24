package de.furkan.perceptionallity.resources;

import de.furkan.perceptionallity.Perceptionallity;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import lombok.Getter;

public class ResourceManager {

  @Getter private final HashMap<String, Resource<?>> resources = new HashMap<>();
  private final HashMap<String, File> resourceFileCache = new HashMap<>();

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
      throw new IllegalArgumentException("Resource key already registered: " + resourceKey);
    }
    resources.put(resourceKey, new Resource<>(resourceValue));
  }

  @SuppressWarnings("unchecked")
  public <T> T getResource(String resourceKey, Class<T> type) {
    Resource<?> resource = resources.get(resourceKey);
    if (resource == null || !type.isInstance(resource.data())) {
      throw new IllegalArgumentException("Invalid or unloaded resource key: " + resourceKey);
    }
    return (T) resource.clone().data();
  }

  public File getResourceFile(String resourceKey, String... resourcePath) {
    String resourcePathString = String.join("/", resourcePath);
    if (resourceFileCache.containsKey(resourcePathString + resourceKey)) {
      return resourceFileCache.get(resourcePathString + resourceKey);
    }
    Optional<URL> resource =
        Optional.ofNullable(getClass().getResource("/" + resourcePathString + "/" + resourceKey));
    if (resource.isEmpty()) {
      throw new RuntimeException(
          "Could not retrieve resource file because of invalid resourceKey or resourcePath. key: "
              + resourceKey
              + " path: "
              + resourcePathString);
    }
    File resourceFile = new File(resource.get().getFile());
    resourceFileCache.put(resourcePathString, resourceFile);
    return resourceFile;
  }
}
