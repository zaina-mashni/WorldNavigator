package com.WorldNavigator;

public enum Direction {
  NORTH,
  EAST,
  SOUTH,
  WEST;

  public int getOppositeDirection(int direction) {
    int noOfDirections = Direction.values().length;
    return (direction + noOfDirections / 2) % noOfDirections;
  }
}
