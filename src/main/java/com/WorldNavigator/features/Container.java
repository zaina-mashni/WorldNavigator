package com.WorldNavigator.features;

import com.WorldNavigator.entities.Item;
import com.WorldNavigator.utils.Pair;

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
    checkIfNull("ItemName", itemName);
    if (!items.containsKey(itemName)) return 0;
    return items.get(itemName).getValue();
  }

  public boolean containsItem(String itemName) {
    checkIfNull("ItemName", itemName);
    return items.containsKey(itemName);
  }

  public Item getItem(String itemName) {
    checkIfNull("ItemName", itemName);
    if (!containsItem(itemName)) {
      throw new IllegalArgumentException("You can not getItem before adding it in class Container");
    }
    return items.get(itemName).getKey();
  }

  public void addOrReplaceItem(Item item, int amount) {
    checkIfNull("Item", item);
    if (containsItem(item.getName())) {
      replaceItem(item.getName(), amount);
    } else {
      addItem(item, amount);
    }
  }

  public void replaceItem(String itemName, int amount) {
    checkIfNull("ItemName", itemName);
    if (!containsItem(itemName)) {
      throw new IllegalArgumentException(
          "Can not replace an item that is not added in class Container");
    }
    if (amount < 0) {
      throw new IllegalArgumentException(
          "Item amount (" + amount + ") should not be negative in class Container");
    }
    if (getItemAmount(itemName) == 0) {
      items.remove(itemName);
    } else {
      items.replace(itemName, new Pair<>(getItem(itemName), amount));
    }
  }

  public void removeAll() {
    items.clear();
  }

  public void addItems(List<Pair<Item, Integer>> items) {
    checkIfNull("Items", items);
    for (Pair<Item, Integer> item : items) {
      addItem(item.getKey(), item.getValue());
    }
  }

  public void addItem(Item item, int amount) {
    checkIfNull("Item", item);
    if (amount <= 0) {
      throw new IllegalArgumentException(
          "Amount can not be less than or equal to zero when adding to inventory in class Container");
    }
    items.put(item.getName(), new Pair<>(item, amount));
  }

  public String getFeatureName() {
    return NAME;
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
      return "No items to show!";
    }
    return stringRepresentation.toString();
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class Container");
    }
  }
}
