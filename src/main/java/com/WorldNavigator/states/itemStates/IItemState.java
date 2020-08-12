package com.WorldNavigator.states.itemStates;

public interface IItemState {
  String getName();

  IItemState handleStateChangeInput(String input);

  String handleStateSpecificInput(String input);

  default void checkIfNull(String key, java.lang.Object value, String className) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class " + className);
    }
  }
}
