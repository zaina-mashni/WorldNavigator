package com.WorldNavigator.states.itemStates;

public class SwitchedOff implements IItemState {
    @Override
    public String getName() {
        return "switchedOff";
    }

    @Override
    public IItemState handleStateChangeInput(String input) {
        checkIfNull("Input",input,"SwitchedOff");
        if(input.equals("useFlashlight")){
            return new SwitchedOn();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(String input) {
        checkIfNull("Input",input,"SwitchedOff");
        if(input.equals("useFlashlight")){
            return "Flashlight is off!";
        }
        return "";
    }

}
