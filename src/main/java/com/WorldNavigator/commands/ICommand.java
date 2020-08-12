package com.WorldNavigator.commands;

import com.WorldNavigator.entities.Item;
import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.features.Container;
import com.WorldNavigator.utils.Pair;

import java.util.List;

public interface ICommand {

  String getName();

  String execute(PlayerInfo player, MapInfo map, List<String> splitCommand);

  default boolean checkNumberOfInput(List<String> splitInput, int noOfRequiredInput) {
    return (splitInput.size() == noOfRequiredInput);
  }

  default Object getObject(PlayerInfo player, String input, int wallIdx) {
    if (isNumberInput(input)) {
      int objectIdx = Integer.parseInt(input) - 1;
      List<Object> objects = player.getCurrentRoom().getWall(wallIdx).getObjects();
      if (objectIdx < 0 || objectIdx >= objects.size()) {
        return null;
      }
      return objects.get(objectIdx);
    }
    return getObjectWithName(player, input, wallIdx);
  }

  default Pair<Item, Integer> getItem(Container container, String input) {
    if (isNumberInput(input)) {
      int itemIdx = Integer.parseInt(input) - 1;
      List<Pair<Item, Integer>> items = container.getItems();
      if (itemIdx < 0 || itemIdx >= items.size()) {
        return null;
      }
      return items.get(itemIdx);
    }
    return getItemWithName(container, input);
  }

  default Pair<Item, Integer> getItemWithName(Container container, String itemName) {
    return container.getItems().stream()
        .filter(item -> item.getKey().getName().equals(itemName))
        .findFirst()
        .orElse(null);
  }

  default Object getObjectWithFeature(PlayerInfo player, String featureName, int wallIdx) {
    return player.getCurrentRoom().getWall(wallIdx).getObjects().stream()
        .filter(object -> object.hasFeature(featureName))
        .findFirst()
        .orElse(null);
  }

  default Object getObjectWithName(PlayerInfo player, String objectName, int wallIdx) {
    return player.getCurrentRoom().getWall(wallIdx).getObjects().stream()
        .filter(object -> object.getName().equals(objectName))
        .findFirst()
        .orElse(null);
  }

  default boolean isNumberInput(String input) {
    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  default void checkArguments(
      PlayerInfo player, MapInfo map, List<String> splitCommand, String className) {
    if (player == null || map == null || splitCommand == null) {
      throw new IllegalArgumentException(
          "Player, map and splitCommand can not be null in class " + className);
    }
  }
}
