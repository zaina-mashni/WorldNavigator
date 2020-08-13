package com.WorldNavigator.states.itemStates;

public class SwitchedOn implements IItemState {
    @Override
    public String getName() {
        return "switchedOn";
    }

    @Override
    public IItemState handleStateChangeInput(String input) {
        checkIfNull("Input",input,"SwitchedOn");
        if(input.equals("useFlashlight")){
            return new SwitchedOff();
        }
        return this;
    }

    @Override
    public String handlePostStateChangeInput(String input) {
        checkIfNull("Input",input,"SwitchedOn");
        if(input.equals("useFlashlight")){
            return "Flashlight is on!";
        }
        return "";
    }

}
