package com.WorldNavigator.behaviours;

import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.features.IFeature;
import com.WorldNavigator.states.objectStates.IObjectState;

import java.util.HashMap;
import java.util.Map;

public class ObjectBehaviour {
  private IObjectState state;
  private Map<String, IFeature> features;

  public ObjectBehaviour(IObjectState state) {
    checkIfNull("State",state);
    this.state = state;
    features = new HashMap<>();
  }

  public boolean hasFeature(String feature) {
    checkIfNull("Feature",feature);
    return features.containsKey(feature);
  }

  public IFeature getFeature(String feature) {
    checkIfNull("Feature",feature);
    if (!hasFeature(feature)) {
      throw new IllegalArgumentException(
          "Can not get a feature that is not in Map in class ObjectBehaviour");
    }
    return features.get(feature);
  }

  public void addOrReplaceFeature(IFeature feature) {
    checkIfNull("Feature",feature);
    if (features.containsKey(feature.getFeatureName())) {
      features.replace(feature.getFeatureName(), feature);
    } else {
      features.put(feature.getFeatureName(), feature);
    }
  }

  public void setState(IObjectState state) {
    checkIfNull("State",state);
    this.state = state;
  }

  public IObjectState getState() {
    return state;
  }

  public void handleStateChangeInput(PlayerInfo player, Object object, String input) {
    checkIfNull("Player",player);
    checkIfNull("Object",object);
    checkIfNull("Input",input);
    state = state.handleStateChangeInput(player, object, input);
  }

  public String handleStateSpecificInput(PlayerInfo player, Object object, String input) {
    checkIfNull("Player",player);
    checkIfNull("Object",object);
    checkIfNull("Input",input);
    return state.handleStateSpecificInput(player, object, input);
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class ObjectBehaviour");
    }
  }

}
