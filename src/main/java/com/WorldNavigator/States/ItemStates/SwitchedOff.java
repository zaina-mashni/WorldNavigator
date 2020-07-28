package com.WorldNavigator.States.ItemStates;

public class SwitchedOff implements IItemState {
    @Override
    public String getName() {
        return "switchedOff";
    }

    @Override
    public IItemState handleStateChangeInput(String input) {
        if(input.equals("useFlashlight")){
            return new SwitchedOn();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(String input) {
        if(input.equals("useFlashlight")){
            return "Flashlight is off!";
        }
        return "";
    }
}
