package de.furkan.perceptionallity.resources;

public record Resource<T>(T data) implements Cloneable {

  @Override
  public Resource<T> clone() throws CloneNotSupportedException {
    return (Resource<T>) super.clone();
  }
}
