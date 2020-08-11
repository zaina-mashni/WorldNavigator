package com.WorldNavigator.states.itemStates;

public interface IItemState {
    String getName();

    IItemState handleStateChangeInput(String input);

    String handleStateSpecificInput(String input);
}
