package com.WorldNavigator.Entities;

import com.WorldNavigator.Behaviours.ObjectBehaviour;
import com.WorldNavigator.Features.IFeature;
import com.WorldNavigator.States.ObjectStates.IObjectState;

public class Object {
    private String name;
    private ObjectBehaviour behaviour;

    public Object(String name, ObjectBehaviour behaviour){
        this.name=name;
        this.behaviour=behaviour;
    }

    public boolean hasFeature(String feature){
        return behaviour.hasFeature(feature);
    }

    public IFeature getFeature(String feature){
        return behaviour.getFeature(feature);
    }

    public void addFeature(IFeature feature){
        behaviour.addFeature(feature);
    }

    public String getName() {
        return name;
    }

    public void setState(IObjectState state){
        behaviour.setState(state);
    }

    public IObjectState getState(){
        return behaviour.getState();
    }


    public void handleStateChangeInput(PlayerInfo player,String command) {
         behaviour.handleStateChangeInput(player, this,command);
    }

    public String handleStateSpecificInput(PlayerInfo player,String input){
        return behaviour.handleStateSpecificInput(player, this,input);
    }
}
