package com.WorldNavigator.entities;

import com.WorldNavigator.behaviours.ObjectBehaviour;
import com.WorldNavigator.features.IFeature;
import com.WorldNavigator.states.objectStates.IObjectState;

public class Object {
  private String name;
  private ObjectBehaviour behaviour;

  public Object(String name, ObjectBehaviour behaviour) {
    checkIfNull("Name", name);
    checkIfNull("Behaviour", behaviour);
    if (name.equals("")) {
      throw new IllegalArgumentException("Name can not be empty in class Object");
    }
    this.name = name;
    this.behaviour = behaviour;
  }

  public boolean hasFeature(String feature) {
    checkIfNull("Feature", feature);
    return behaviour.hasFeature(feature);
  }

  public IFeature getFeature(String feature) {
    checkIfNull("Feature", feature);
    return behaviour.getFeature(feature);
  }

  public void addOrReplaceFeature(IFeature feature) {
    checkIfNull("Feature", feature);
    behaviour.addOrReplaceFeature(feature);
  }

  public String getName() {
    return name;
  }

  public void setState(IObjectState state) {
    checkIfNull("State", state);
    behaviour.setState(state);
  }

  public IObjectState getState() {
    return behaviour.getState();
  }

  public void handleStateChangeInput(PlayerInfo player, String input) {
    checkIfNull("Player", player);
    checkIfNull("Input", input);
    behaviour.handleStateChangeInput(player, this, input);
  }

  public String handlePostStateChangeInput(PlayerInfo player, String input) {
    checkIfNull("Player", player);
    checkIfNull("Input", input);
    return behaviour.handlePostStateChangeInput(player, this, input);
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class Object");
    }
  }
}
