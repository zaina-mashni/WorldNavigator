package com.WorldNavigator.features;

import com.WorldNavigator.entities.Room;

public class Passage implements IFeature {
  private Room firstRoom;
  private Room secondRoom;
  int firstRoomWallIdx;
  int secondRoomWallIdx;
  private static final String NAME = "passage";

  public Passage(Room firstRoom, int firstRoomWallIdx, Room secondRoom, int secondRoomWallIdx) {
    checkIfNull("FirstRoom", firstRoom,"Passage");
    checkIfNull("SecondRoom", secondRoom,"Passage");
    this.firstRoom = firstRoom;
    this.secondRoom = secondRoom;
    this.firstRoomWallIdx = firstRoomWallIdx;
    this.secondRoomWallIdx = secondRoomWallIdx;
  }

  public Room getOppositeRoom(Room room) {
    checkIfNull("Room", room,"Passage");
    if (firstRoom == room) return secondRoom;
    return firstRoom;
  }

  public int getOppositeWallIdx(int wallIdx) {
    if (wallIdx < 0 || wallIdx >= 4) {
      throw new IndexOutOfBoundsException(
          "WallIndex can not be less than 0 or greater than 3 in class Passage");
    }
    if (firstRoomWallIdx == wallIdx) return secondRoomWallIdx;
    return firstRoomWallIdx;
  }

  public String getFeatureName() {
    return NAME;
  }

}
