package com.WorldNavigator.utils;

public class Pair<K, V> {

  private final K firstElement;
  private final V secondElement;

  public Pair(K firstElement, V secondElement) {
    checkIfNull("FirstElement",firstElement);
    checkIfNull("SecondElement",secondElement);
    this.firstElement = firstElement;
    this.secondElement = secondElement;
  }

  public K getKey() {
    return firstElement;
  }

  public V getValue() {
    return secondElement;
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class Pair");
    }
  }
}
