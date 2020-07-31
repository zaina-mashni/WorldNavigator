package com.WorldNavigator.Features;

import com.WorldNavigator.Entities.Item;
import com.WorldNavigator.Pair;

import java.util.List;

public class Trade implements IFeature {
    private static final String NAME="trade";
    private Container inventory;

    public Trade(Container inventory){
        this.inventory=inventory;
    }

    public Item buy(String itemName){
        if(!inventory.containsItem(itemName)){
            throw new IllegalArgumentException("item to buy is not in the sellers inventory.");
        }
        int itemAmountAfterTransaction=inventory.getItemAmount(itemName)-1;
        if(itemAmountAfterTransaction<0){
            throw new IllegalArgumentException("can not buy an item that has amount of less than 1.");
        }
        Item item=inventory.getItem(itemName);
        inventory.replaceItem(itemName,itemAmountAfterTransaction);
        return item;
    }

    public void sell(Item item){
        if(inventory.containsItem(item.getName())){
            inventory.replaceItem(item.getName(),inventory.getItemAmount(item.getName())+1);
        }
        else {
            inventory.addItem(item,1);
        }
    }
    public Container getInventory(){
        return inventory;
    }


    public List<Pair<Item,Integer>> listItems(){
        return inventory.getItems();
    }

    @Override
    public String getFeatureName() {
        return NAME;
    }

}
