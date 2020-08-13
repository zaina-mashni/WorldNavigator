package com.WorldNavigator.entities;

import com.WorldNavigator.services.GameService;
import com.WorldNavigator.states.roomStates.roomAvailabilityStates.Empty;
import com.WorldNavigator.states.roomStates.roomAvailabilityStates.IRoomAvailabilityState;
import com.WorldNavigator.states.roomStates.roomAvailabilityStates.Open;
import com.WorldNavigator.states.roomStates.roomStatusStates.IRoomStatusState;

import java.util.*;

public class Room {

  private List<Wall> walls;
  private Stack<IRoomAvailabilityState> availabilityStates;
  private IRoomStatusState statusState;
  private GameService gameService;
  private int roomId;

  public Room(IRoomStatusState statusState, GameService gameService, int roomId) {
    checkIfNull("StatusState", statusState);
    checkIfNull("GameService", gameService);
    walls = new ArrayList<>(Arrays.asList(new Wall(), new Wall(), new Wall(), new Wall()));
    availabilityStates = new Stack<>();
    availabilityStates.push(new Empty());
    this.statusState = statusState;
    this.gameService = gameService;
    this.roomId = roomId;
  }

  public int getRoomId() {
    return roomId;
  }

  public void popAvailabilityState() {
    if (availabilityStates.size() < 2) {
      throw new IllegalStateException(
          "AvailabilityState stack size can not be less than 1 in class Room");
    }
    this.availabilityStates.pop();
  }

  public IRoomStatusState getStatusState() {
    return statusState;
  }

  public IRoomAvailabilityState getAvailabilityState() {
    return availabilityStates.peek();
  }

  public Wall getWall(int wallIndex) {
    checkWallIndex(wallIndex);
    return walls.get(wallIndex);
  }

  public void handleStatusStateChangeInput(PlayerInfo player, String input) {
    checkIfNull("Player", player);
    checkIfNull("Input", input);
    statusState = statusState.handleStateChangeInput(player, input);
  }

  public String handlePostStatusStateChangeInput(PlayerInfo player, String input) {
    checkIfNull("Player", player);
    checkIfNull("Input", input);
    return statusState.handlePostStateChangeInput(player, input);
  }

  public String handlePostAvailabilityStateChangeInput(PlayerInfo player, String input) {
    checkIfNull("Player", player);
    checkIfNull("Input", input);
    return availabilityStates.peek().handlePostStateChangeInput(player, input);
  }

  public void handleAvailabilityStateChangeInput(PlayerInfo player, String input) {
    checkIfNull("Player", player);
    checkIfNull("Input", input);
    IRoomAvailabilityState availabilityState =
        availabilityStates.peek().handleStateChangeInput(player, input);
    availabilityStates.push(availabilityState);
    if (availabilityStates.peek().getName().equals("full")) {
      Thread thread = new Thread(() -> gameService.onRoomFull(player, availabilityStates.peek()));
      thread.start();
    }
  }

  public void addToWall(int wallIdx, Object object) {
    checkWallIndex(wallIdx);
    checkIfNull("Object", object);
    walls.get(wallIdx).addOrReplaceObject(object);
  }

  public void returnToOpenState() {
    availabilityStates.clear();
    availabilityStates.push(new Empty());
    availabilityStates.push(new Open());
  }

  private void checkWallIndex(int index) {
    if (index < 0 || index > 4) {
      throw new IndexOutOfBoundsException(
          "Wall index (" + index + ") can not be less than 0 or greater than 4 in class Room");
    }
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class Room");
    }
  }

}
