package com.WorldNavigator.States.ItemStates;

public class Static implements IItemState {
    @Override
    public String getName() {
        return "static";
    }

    @Override
    public IItemState handleStateChangeInput(String input) {
        //no current commands affect static state items
        return this;
    }

    @Override
    public String handleStateSpecificInput(String input) {
        //no current commands on static state items
        return "";
    }
}
