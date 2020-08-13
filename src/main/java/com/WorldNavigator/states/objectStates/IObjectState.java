package com.WorldNavigator.states.objectStates;

import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;

public interface IObjectState {
  IObjectState handleStateChangeInput(PlayerInfo player, Object object, String input);

  String handlePostStateChangeInput(PlayerInfo player, Object object, String input);

  String getName();

  default void checkIfNull(String key, java.lang.Object value, String className) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class " + className);
    }
  }
}
