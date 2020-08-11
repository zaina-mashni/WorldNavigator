package com.WorldNavigator.entities;

import java.util.List;

public class MapInfo {
  private List<Room> rooms;

  public MapInfo(List<Room> rooms) {
    checkIfNull("Rooms", rooms);
    this.rooms = rooms;
  }

  public Room getRoom(int roomIndex) {
    if (roomIndex < 0 || roomIndex >= rooms.size()) {
      throw new IndexOutOfBoundsException(
          "RoomIndex provided ("
              + roomIndex
              + ") is not between 0 and "
              + (rooms.size() - 1)
              + " in class MapInfo");
    }
    return rooms.get(roomIndex);
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class MapInfo");
    }
  }
}
