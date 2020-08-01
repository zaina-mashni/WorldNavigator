package com.WorldNavigator;

public class Pair<K, V> {

  private final K firstElement;
  private final V secondElement;

  public Pair(K firstElement, V secondElement) {
    this.firstElement = firstElement;
    this.secondElement = secondElement;
  }

  public K getKey() {
    return firstElement;
  }

  public V getValue() {
    return secondElement;
  }
}
