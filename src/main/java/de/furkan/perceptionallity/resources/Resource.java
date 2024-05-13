package de.furkan.perceptionallity.resources;

public record Resource<T>(T data) implements Cloneable {

  @Override
  public Resource<T> clone() {
    Resource<T> clone;
    try {
      clone = (Resource<T>) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
    return clone;
  }
}
