package com.WorldNavigator.Behaviours;

import com.WorldNavigator.States.ItemStates.IItemState;

public class ItemBehaviour {
    private int cost;
    private IItemState state;

    public ItemBehaviour(IItemState state,int cost){
        this.cost=cost;
        this.state=state;
    }

    public int getCost() {
        return cost;
    }

    public IItemState getState() {
        return state;
    }

    public void handleStateChangeInput(String input){
        state=state.handleStateChangeInput(input);
    }

    public String handleStateSpecificInput (String input){
        return state.handleStateSpecificInput(input);
    }
}
