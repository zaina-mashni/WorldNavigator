package com.WorldNavigator.Factories;

import com.WorldNavigator.Behaviours.ItemBehaviour;
import com.WorldNavigator.Entities.Item;
import com.WorldNavigator.States.ItemStates.Static;
import com.WorldNavigator.States.ItemStates.SwitchedOff;
import com.WorldNavigator.States.ItemStates.SwitchedOn;
import org.springframework.stereotype.Component;

@Component
public class ItemFactory {

    public Item buildItem(String itemName, String state, int cost){
        switch (state){
            case "static":
                return new Item(itemName,new ItemBehaviour(new Static(),cost));
            case "switchedOn":
                return new Item(itemName,new ItemBehaviour(new SwitchedOn(),cost));
            case "switchedOff":
                return new Item(itemName,new ItemBehaviour(new SwitchedOff(),cost));
            default:
                throw new IllegalArgumentException("state is not found in ItemFactory.buildItem");
        }
    }
}
