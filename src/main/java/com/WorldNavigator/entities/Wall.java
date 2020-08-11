package com.WorldNavigator.entities;

import java.util.*;

public class Wall {

  private Map<String, Object> objects;

  public Wall() {
    objects = new HashMap<>();
  }

  public List<Object> getObjects() {
    return new ArrayList<>(objects.values());
  }

  public Object getObject(String objectName) {
    checkIfNull("ObjectName", objectName);
    if (!containsObject(objectName)) {
      throw new IllegalArgumentException("You can not getObject before adding it in class Wall");
    }
    return objects.get(objectName);
  }

  public boolean containsObject(String objectName) {
    checkIfNull("ObjectName", objectName);
    return objects.containsKey(objectName);
  }

  public void addOrReplaceObject(Object object) {
    checkIfNull("Object", object);
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

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class Wall");
    }
  }
}
