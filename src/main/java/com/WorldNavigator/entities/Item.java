package com.WorldNavigator.entities;

import com.WorldNavigator.behaviours.ItemBehaviour;
import com.WorldNavigator.states.itemStates.IItemState;

public class Item {
  private String name;
  private ItemBehaviour behaviour;

  public Item(String name, ItemBehaviour behaviour) {
    checkIfNull("Behaviour", behaviour);
    checkIfNull("Name", name);
    if (name.equals("")) {
      throw new IllegalArgumentException("Name can not be empty in class Item");
    }
    this.name = name;
    this.behaviour = behaviour;
  }

  public String getName() {
    return name;
  }

  public int getCost() {
    return behaviour.getCost();
  }

  public IItemState getState() {
    return behaviour.getState();
  }

  public void handleStateChangeInput(String input) {
    checkIfNull("Input", input);
    behaviour.handleStateChangeInput(input);
  }

  public String handlePostStateChangeInput(String input) {
    checkIfNull("Input", input);
    return behaviour.handlePostStateChangeInput(input);
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class Item");
    }
  }
}
