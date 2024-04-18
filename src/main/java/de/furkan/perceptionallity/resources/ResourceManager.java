package de.furkan.perceptionallity.resources;

import de.furkan.perceptionallity.Perceptionallity;
import java.util.HashMap;
import lombok.Getter;

@Getter
public class ResourceManager {

  private final HashMap<String, Resource<?>> resources = new HashMap<>();

  public <T> void registerResource(String resourceKey, T resourceValue) {
    Perceptionallity.getLogger().info("Loading resource: " + resourceKey);
    if (resources.containsKey(resourceKey)) {
      throw new IllegalArgumentException("Resource key already registered: " + resourceKey);
    }
    resources.put(resourceKey, new Resource<>(resourceValue));
  }

  @SuppressWarnings("unchecked")
  public <T> T getResource(String resourceKey, Class<T> type) {
    Perceptionallity.getLogger()
        .info("Retrieving resource: " + resourceKey + " (" + type.getSimpleName() + ")");
    Resource<?> resource = resources.get(resourceKey);
    if (resource == null || !type.isInstance(resource.data())) {
      throw new IllegalArgumentException("Invalid or unloaded resource key: " + resourceKey);
    }
    return (T) resource.data();
  }
}
