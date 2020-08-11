package com.WorldNavigator.entities;

import com.WorldNavigator.Direction;
import com.WorldNavigator.behaviours.ItemBehaviour;
import com.WorldNavigator.features.Container;
import com.WorldNavigator.states.itemStates.Static;
import com.WorldNavigator.utils.Pair;
import com.WorldNavigator.states.playerStates.*;

import java.util.List;
import java.util.Stack;

public class PlayerInfo {

  Stack<IPlayerState> state;
  String username;
  String worldName;
  Room currentRoom;
  int facingDirection;
  boolean joinedWorld;
  Container inventory;

  public PlayerInfo(String username) {
    checkIfNull("Username", username);
    if (username.equals("")) {
      throw new IllegalArgumentException("Username can not be empty in class PlayerInfo");
    }
    this.username = username;
    inventory = new Container();
    state = new Stack<>();
    setWorld("");
    returnToRoomState();
    setFacingDirection(0);
  }

  public void returnToRoomState() {
    state.clear();
    state.push(new RoomLevel());
  }

  public void setFacingDirection(int facingDirection) {
    if (facingDirection < 0 || facingDirection >= Direction.values().length) {
      System.out.println("directions size: " + Direction.values().length);
      throw new IndexOutOfBoundsException(
          "FacingDirection provided ("
              + facingDirection
              + ") is not between 0 and 4 in class PlayerInfo");
    }
    this.facingDirection = facingDirection;
  }

  public void setCurrentRoom(Room currentRoom) {
    this.currentRoom = currentRoom;
  }

  public void pushState(IPlayerState state) {
    checkIfNull("State", state);
    this.state.push(state);
  }

  public int getFacingDirection() {
    return facingDirection;
  }

  public Room getCurrentRoom() {
    if (currentRoom == null) {
      throw new IllegalStateException(
          "You can not getCurrentRoom before setting it in class PlayerInfo");
    }
    return currentRoom;
  }

  public IPlayerState getCurrentState() {
    return state.peek();
  }

  public String getWorldName() {
    return worldName;
  }

  public String getUsername() {
    return username;
  }

  public int getGoldAmount() {
    return inventory.getItemAmount("gold");
  }

  public void updateGoldAmount(int amount) {
    int amountAfterTransaction = inventory.getItemAmount("gold") + amount;
    if (amountAfterTransaction < 0) {
      throw new IllegalArgumentException(
          "Player's gold amount can not be less than zero in class PlayerInfo");
    }
    inventory.addOrReplaceItem(
        new Item("gold", new ItemBehaviour(new Static(), 1)), amountAfterTransaction);
  }

  public void setWorld(String worldName) {
    checkIfNull("WorldName", worldName);
    if (worldName.equals("")) {
      this.worldName = "";
      joinedWorld = false;
    } else {
      this.worldName = worldName;
      joinedWorld = true;
    }
  }

  public void quitWorld() {
    setWorld("");
    setCurrentRoom(null);
    clearInventory();
    returnToRoomState();
  }

  public void clearInventory() {
    inventory.removeAll();
  }

  public boolean hasItemInInventory(String itemName) {
    checkIfNull("ItemName", itemName);
    return inventory.containsItem(itemName);
  }

  public Item getItemFromInventory(String itemName) {
    checkIfNull("ItemName", itemName);
    if (hasItemInInventory(itemName)) {
      return inventory.getItem(itemName);
    }
    throw new IllegalArgumentException(
        "Can not get a item from inventory that is not in inventory in class PlayerInfo");
  }

  public Container getInventory() {
    return inventory;
  }

  public void addToInventory(List<Pair<Item, Integer>> items) {
    checkIfNull("Items", items);
    items.forEach(item -> inventory.addOrReplaceItem(item.getKey(), item.getValue()));
  }

  public void popState() {
    if (state.size() < 2) {
      throw new IllegalStateException(
          "State stack size can not be less than 1 in class PlayerInfo");
    }
    state.pop();
  }

  public int convertInventoryToGoldAmount() {
    int totalGold = 0;
    List<Pair<Item, Integer>> items = inventory.getItems();
    for (Pair<Item, Integer> item : items) {
      totalGold += item.getKey().getCost() * item.getValue();
    }
    return totalGold;
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class PlayerInfo");
    }
  }
}
