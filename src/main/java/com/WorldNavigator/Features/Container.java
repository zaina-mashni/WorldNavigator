package com.WorldNavigator.Features;

import com.WorldNavigator.Entities.Item;
import com.WorldNavigator.Pair;

import java.util.*;

public class Container implements IFeature {
  private Map<String, Pair<Item, Integer>> items;
  private static final String NAME = "container";

  public Container() {
    items = Collections.synchronizedMap(new HashMap<>());
  }

  public Map<String, Pair<Item, Integer>> getInventory() {
    return items;
  }

  public List<Pair<Item, Integer>> getItems() {
    List<Pair<Item, Integer>> tmpItems = new ArrayList<>();
    for (Map.Entry<String, Pair<Item, Integer>> item : items.entrySet()) {
      tmpItems.add(new Pair<>(item.getValue().getKey(), item.getValue().getValue()));
    }
    return tmpItems;
  }

  public int getItemAmount(String itemName) {
    checkItemName(itemName);
    if (!items.containsKey(itemName)) return 0;
    return items.get(itemName).getValue();
  }

  public boolean containsItem(String itemName) {
    checkItemName(itemName);
    return items.containsKey(itemName);
  }

  public Item getItem(String itemName) {
    checkItemName(itemName);
    checkItemAvailability(itemName);
    return items.get(itemName).getKey();
  }

  public void replaceItem(String itemName, int amount) {
    checkItemName(itemName);
    checkItemAvailability(itemName);
    items.replace(itemName, new Pair<>(getItem(itemName), amount));
    if (getItemAmount(itemName) < 0) {
      throw new IllegalArgumentException("item amount should not be negative.");
    } else if (getItemAmount(itemName) == 0 && !itemName.equals("gold")) {
      items.remove(itemName);
    }
  }

  public void removeAll() {
    items.clear();
  }

  public void addItems(List<Pair<Item, Integer>> items) {
    for (Pair<Item, Integer> item : items) {
      addItem(item.getKey(), item.getValue());
    }
  }

  public void addItem(Item item, int amount) {
    Objects.requireNonNull(
        item, "Item can not be null when adding to container in Container.addItem.");
    if (!item.getName().equals("gold")) {
      checkItemAmount(amount);
    } else {
      if (amount < 0) {
        throw new IllegalArgumentException(
            "gold amount "
                + amount
                + " can not be a negative integer when adding to container in Container.addItem");
      }
    }
    if (!items.containsKey(item.getName())) items.put(item.getName(), new Pair<>(item, amount));
    else items.replace(item.getName(), new Pair<>(item, getItemAmount(item.getName()) + amount));
  }

  public String getFeatureName() {
    return NAME;
  }

  private void checkItemName(String itemName) {
    if (itemName.isEmpty()) {
      throw new IllegalArgumentException(
          itemName + " can not be empty when getting amount from container");
    }
  }

  private void checkItemAvailability(String itemName) {
    if (!items.containsKey(itemName))
      throw new IllegalArgumentException(
          itemName
              + " does not exist in container in Container.checkIemAvailability in Container.checkItemAvailability");
  }

  private void checkItemAmount(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException(
          amount
              + " has to be a non negative integer when adding to container in Container.checkItemAmount.");
    }
  }

  @Override
  public String toString() {
    StringBuilder stringRepresentation = new StringBuilder();
    String prefix = "";
    for (Map.Entry<String, Pair<Item, Integer>> item : items.entrySet()) {
      stringRepresentation.append(prefix);
      prefix = " | ";
      stringRepresentation
          .append("Name: ")
          .append(item.getKey())
          .append(", Cost: ")
          .append(item.getValue().getKey().getCost())
          .append(", Amount: ")
          .append(item.getValue().getValue());
    }
    if (stringRepresentation.length() == 0) {
      return "No items in inventory!";
    }
    return stringRepresentation.toString();
  }
}
