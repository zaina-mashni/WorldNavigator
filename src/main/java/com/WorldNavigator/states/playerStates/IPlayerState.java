package com.WorldNavigator.states.playerStates;

import com.WorldNavigator.commands.ICommand;

import java.util.List;

public interface IPlayerState {

  List<ICommand> getAvailableCommands();

  String getName();

  String getDisplayName();

  default void checkIfNull(String key, java.lang.Object value, String className) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class " + className);
    }
  }
}
