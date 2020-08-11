package com.WorldNavigator.states.itemStates;

public class SwitchedOn implements IItemState {
    @Override
    public String getName() {
        return "switchedOn";
    }

    @Override
    public IItemState handleStateChangeInput(String input) {
        checkIfNull("Input",input);
        if(input.equals("useFlashlight")){
            return new SwitchedOff();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(String input) {
        checkIfNull("Input",input);
        if(input.equals("useFlashlight")){
            return "Flashlight is on!";
        }
        return "";
    }

    private void checkIfNull(String key, java.lang.Object value) {
        if (value == null) {
            throw new IllegalArgumentException(key + " can not be null in class SwitchedOn");
        }
    }
}
