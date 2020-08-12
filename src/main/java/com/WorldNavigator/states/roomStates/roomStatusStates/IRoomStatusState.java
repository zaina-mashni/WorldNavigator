package com.WorldNavigator.states.roomStates.roomStatusStates;

import com.WorldNavigator.entities.PlayerInfo;

public interface IRoomStatusState {
  IRoomStatusState handleStateChangeInput(PlayerInfo player, String input);

  String handleStateSpecificInput(PlayerInfo player, String input);

  String getName();

  default void checkIfNull(String key, java.lang.Object value, String className) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class " + className);
    }
  }
}
