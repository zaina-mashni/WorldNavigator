package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.Item;
import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Features.Container;
import com.WorldNavigator.Services.CommandService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ICommand {
  String INVALID = "Invalid input!";

  default boolean checkNumberOfInput(List<String> splitInput, int noOfRequiredInput) {
    return (splitInput.size() == noOfRequiredInput);
  }

  default boolean checkIfObjectOnWall(PlayerInfo player, String objectName, int wallIdx) {
    return player.getCurrentRoom().getWall(wallIdx).containsObject(objectName);
  }

  default Object getObject(PlayerInfo player, String input, int wallIdx) {
    if (isNumberInput(input)) {
      int objectIdx = Integer.parseInt(input) -1;
      List<Object> objects = player.getCurrentRoom().getWall(wallIdx).getObjects();
      if (objectIdx < 0 || objectIdx >= objects.size()) {
        return null;
      }
      return objects.get(objectIdx);
    }
    return getObjectWithName(player,input,wallIdx);
  }

  default Pair<Item, Integer> getItem(Container container, String input) {
    if (isNumberInput(input)) {
      int itemIdx = Integer.parseInt(input)-1;
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

  String getName();

  String execute(PlayerInfo player, MapInfo map, List<String> splitCommand);


}
