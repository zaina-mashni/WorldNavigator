package com.WorldNavigator.States.ItemStates;

public class SwitchedOn implements IItemState {
    @Override
    public String getName() {
        return "switchedOn";
    }

    @Override
    public IItemState handleStateChangeInput(String input) {
        if(input.equals("useFlashlight")){
            return new SwitchedOff();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(String input) {
        if(input.equals("useFlashlight")){
            return "Flashlight is on!";
        }
        return "";
    }
}
