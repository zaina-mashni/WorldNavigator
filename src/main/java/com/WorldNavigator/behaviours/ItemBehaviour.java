package com.WorldNavigator.behaviours;

import com.WorldNavigator.states.itemStates.IItemState;

public class ItemBehaviour {
  private int cost;
  private IItemState state;

  public ItemBehaviour(IItemState state, int cost) {
    checkIfNull("State", state);
    if (cost <= 0) {
      throw new IllegalArgumentException(
          "Cost (" + cost + ") can not be less that or equal to zero in class ItemBehaviour");
    }
    this.cost = cost;
    this.state = state;
  }

  public int getCost() {
    return cost;
  }

  public IItemState getState() {
    return state;
  }

  public void handleStateChangeInput(String input) {
    checkIfNull("Input", input);
    state = state.handleStateChangeInput(input);
  }

  public String handlePostStateChangeInput(String input) {
    checkIfNull("Input", input);
    return state.handlePostStateChangeInput(input);
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class ItemBehaviour");
    }
  }
}
