package com.WorldNavigator.Features;

import com.WorldNavigator.Entities.Item;

public class Lock implements IFeature{
    private static final String NAME="lock";
    private Item key;

    public Lock(Item key){
        this.key= key;
    }

    public String getKeyName(){
        return key.getName();
    }

    public boolean checkIfMatch(Item playerKey){
        return key.getName().equals(playerKey.getName());
    }

    public String getFeatureName(){
        return NAME;
    }
}
