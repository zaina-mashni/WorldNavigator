package com.WorldNavigator.states.itemStates;

public class Static implements IItemState {
    @Override
    public String getName() {
        return "static";
    }

    @Override
    public IItemState handleStateChangeInput(String input) {
        checkIfNull("Input",input,"ItemState.Static");
        //no current commands affect static state items
        return this;
    }

    @Override
    public String handleStateSpecificInput(String input) {
        checkIfNull("Input",input,"ItemState.Static");
        //no current commands on static state items
        return "";
    }

}
