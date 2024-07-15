package de.furkan.perceptionallity.resources;

public record Resource<T>(T data) implements Cloneable {

  @Override
  @SuppressWarnings("unchecked")
  public Resource<T> clone() throws CloneNotSupportedException {
    return (Resource<T>) super.clone();
  }
}
