package com.WorldNavigator.features;

import com.WorldNavigator.entities.Item;

public class Lock implements IFeature {
  private static final String NAME = "lock";
  private Item key;

  public Lock(Item key) {
    checkIfNull("Key",key,"Lock");
    this.key = key;
  }

  public String getKeyName() {
    return key.getName();
  }

  public String getFeatureName() {
    return NAME;
  }

}
