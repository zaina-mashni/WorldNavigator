package com.WorldNavigator.Entities;

import com.WorldNavigator.Behaviours.ItemBehaviour;
import com.WorldNavigator.States.ItemStates.IItemState;

public class Item {
  private String name;
  private ItemBehaviour behaviour;

  public Item(String name, ItemBehaviour behaviour) {
    this.name = name;
    this.behaviour = behaviour;
  }

  public String getName() {
    return name;
  }

  public int getCost(){
      return behaviour.getCost();
  }

    public IItemState getState() {
    return behaviour.getState();
    }

  public void handleStateChangeInput(String input) {
    behaviour.handleStateChangeInput(input);
  }

  public String handleStateSpecificInput(String input){
    return behaviour.handleStateSpecificInput(input);
  }
}
