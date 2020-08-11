package com.WorldNavigator.factories;

import com.WorldNavigator.behaviours.ItemBehaviour;
import com.WorldNavigator.entities.Item;
import com.WorldNavigator.exceptions.MissingStateInFactory;
import com.WorldNavigator.states.itemStates.Static;
import com.WorldNavigator.states.itemStates.SwitchedOff;
import com.WorldNavigator.states.itemStates.SwitchedOn;
import org.springframework.stereotype.Component;

@Component
public class ItemFactory {

    public Item buildItem(String itemName, String state, int cost) {
        checkIfNull("State",state);
        switch (state){
            case "static":
                return new Item(itemName,new ItemBehaviour(new Static(),cost));
            case "switchedOn":
                return new Item(itemName,new ItemBehaviour(new SwitchedOn(),cost));
            case "switchedOff":
                return new Item(itemName,new ItemBehaviour(new SwitchedOff(),cost));
            default:
                throw new MissingStateInFactory(state+" state is not found in class ItemFactory");
        }
    }

    private void checkIfNull(String key, java.lang.Object value) {
        if (value == null) {
            throw new IllegalArgumentException(key + " can not be null in class ItemFactory");
        }
    }
}
