package com.WorldNavigator.Entities;

import com.WorldNavigator.Features.Container;
import com.WorldNavigator.States.PlayerStates.*;
import javafx.util.Pair;

import java.util.List;
import java.util.Stack;

public class PlayerInfo {

  Stack<IPlayerState> state = new Stack<>();
  String username;
  String worldName;
  Room currentRoom;
  int facingDirection;
  boolean joinedWorld;
  Container inventory;

  public PlayerInfo(String username) {
    this.worldName = "";
    this.username = username;
    state.push(new RoomLevel());
    this.joinedWorld = false;
    inventory=new Container();
  }

  public void setFacingDirection(int facingDirection) {
    this.facingDirection = facingDirection;
  }

  public void setCurrentRoom(Room currentRoom) {
    this.currentRoom = currentRoom;
  }

  public void pushState(IPlayerState state) {
    this.state.push(state);
  }

  public int getFacingDirection() {
    return facingDirection;
  }

  public Room getCurrentRoom() {
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

  public boolean isJoinedWorld() {
    return joinedWorld;
  }

  public void quitWorld() {
    this.worldName="";
    this.joinedWorld = false;
  }

  public int getGoldAmount() {
    return inventory.getAmount("gold");
  }

  public void updateGoldAmount(int amount) {
    int amountAfterTransaction = inventory.getAmount("gold") + amount;
    if (amountAfterTransaction < 0) {
      throw new IllegalArgumentException("player's gold amount can not be less than zero");
    }
    inventory.replaceItem("gold", amountAfterTransaction);
  }

  public void setWorld(String worldName) {
    if(worldName.equals("")){
      this.worldName="";
      joinedWorld=false;
    }
    else {
      this.worldName=worldName;
      joinedWorld=true;
    }
  }

  public boolean hasItemInInventory(String itemName) {
    return inventory.containsItem(itemName);
  }

  public Item getItemFromInventory(String itemName) {
    if (hasItemInInventory(itemName)) {
      return inventory.getItem(itemName);
    }
    return null;
  }

  public Container getInventory() {
    return inventory;
  }

  public void addToInventory(List<Pair<Item, Integer>> items) {
    items.forEach(item -> inventory.addItem(item.getKey(), item.getValue()));
  }

  public void popState() {
    state.pop();
  }

  public int getWorth() {
    int totalGold=0;
    List<Pair<Item,Integer>> items = inventory.getItems();
    for (Pair<Item,Integer> item : items) {
      totalGold+=item.getKey().getCost()*item.getValue();
    }
    return totalGold;
  }


}
