package com.WorldNavigator.Entities;

import java.util.*;

public class Wall {

  private Map<String, Object> objects;

  public Wall() {
    objects = new HashMap<>();
  }

  public List<Object> getObjects() {
    return new ArrayList<>(objects.values());
  }

  public Object getObject(String entityName) {
    checkObjectName(entityName);
    return objects.get(entityName);
  }

  public boolean containsObject(String entityName) {
    return objects.containsKey(entityName);
  }

  private void checkObjectName(String entityName) {
    if (!objects.containsKey(entityName)) {
      throw new IllegalArgumentException("entity does not exist in wall.");
    }
  }

  public void addObject(Object object) {
    if (containsObject(object.getName())) {
      objects.replace(object.getName(), object);
    }
    objects.put(object.getName(), object);
  }

  @Override
  public String toString() {
    StringBuilder stringRepresentation = new StringBuilder();
    String prefix = "";
    int idx = 1;
    for (Map.Entry<String, Object> object : objects.entrySet()) {
      stringRepresentation.append(prefix);
      prefix = " | ";
      stringRepresentation.append("(").append(idx).append(") ").append(object.getKey());
      idx++;
    }
    if (stringRepresentation.length() == 0) {
      return "Wall is empty!";
    }
    return stringRepresentation.toString();
  }
}
