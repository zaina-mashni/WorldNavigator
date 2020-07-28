package com.WorldNavigator.Behaviours;

import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Features.IFeature;
import com.WorldNavigator.States.ObjectStates.IObjectState;

import java.util.HashMap;
import java.util.Map;

public class ObjectBehaviour {
    private IObjectState state;
    private Map<String, IFeature> features;

    public ObjectBehaviour(IObjectState state){
        this.state=state;
        features=new HashMap<>();
    }

    public boolean hasFeature(String feature){
        return features.containsKey(feature);
    }

    public IFeature getFeature(String feature){
        return features.get(feature);
    }

    public void addFeature(IFeature feature){
        if(features.containsKey(feature.getFeatureName())){
            features.replace(feature.getFeatureName(),feature);
        }
        features.put(feature.getFeatureName(),feature);
    }

    public void setState(IObjectState state){
        this.state=state;
    }

    public IObjectState getState() {
        return state;
    }

    public void handleStateChangeInput(PlayerInfo player,Object object, String command) {
        state= state.handleStateChangeInput(player,object,command);
    }

    public String handleStateSpecificInput(PlayerInfo player,Object object, String command) {
        return state.handleStateSpecificInput(player,object,command);
    }
}
