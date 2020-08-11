package com.WorldNavigator.features;

import com.WorldNavigator.entities.Item;

public class Lock implements IFeature {
  private static final String NAME = "lock";
  private Item key;

  public Lock(Item key) {
    checkIfNull("Key",key);
    this.key = key;
  }

  public String getKeyName() {
    return key.getName();
  }

  public String getFeatureName() {
    return NAME;
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class Lock");
    }
  }
}
