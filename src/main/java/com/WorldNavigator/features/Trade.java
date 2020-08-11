package com.WorldNavigator.features;

import com.WorldNavigator.entities.Item;

public class Trade implements IFeature {
  private static final String NAME = "trade";
  private Container inventory;

  public Trade(Container inventory) {
    checkIfNull("Inventory", inventory);
    this.inventory = inventory;
  }

  public Item buy(String itemName) {
    checkIfNull("ItemName", itemName);
    if (!inventory.containsItem(itemName)) {
      throw new IllegalArgumentException(
          "item to buy is not in the sellers inventory in class Trade.");
    }
    int itemAmountAfterTransaction = inventory.getItemAmount(itemName) - 1;
    Item item = inventory.getItem(itemName);
    inventory.replaceItem(itemName, itemAmountAfterTransaction);
    return item;
  }

  public void sell(Item item) {
    checkIfNull("Item", item);
    inventory.addOrReplaceItem(item, inventory.getItemAmount(item.getName()) + 1);
  }

  public Container getInventory() {
    return inventory;
  }

  @Override
  public String getFeatureName() {
    return NAME;
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class Trade");
    }
  }
}
