package com.WorldNavigator.States.ItemStates;

public interface IItemState {
    String getName();

    IItemState handleStateChangeInput(String input);

    String handleStateSpecificInput(String input);
}
