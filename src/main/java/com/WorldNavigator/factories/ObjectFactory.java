package com.WorldNavigator.factories;

import com.WorldNavigator.behaviours.ObjectBehaviour;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.exceptions.MissingStateInFactory;
import com.WorldNavigator.states.objectStates.*;
import org.springframework.stereotype.Component;

@Component
public class ObjectFactory {
  public Object buildObject(String objectName, String state) {
    checkIfNull("State",state);
    switch (state) {
      case "static":
        return new Object(objectName, new ObjectBehaviour(new Static()));
      case "locked":
        return new Object(objectName, new ObjectBehaviour(new Locked()));
      case "unlocked":
        return new Object(objectName, new ObjectBehaviour(new Unlocked()));
      case "opened":
        return new Object(objectName, new ObjectBehaviour(new Opened()));
      case "closed":
        return new Object(objectName, new ObjectBehaviour(new Closed()));
      default:
        throw new MissingStateInFactory(state+" state is not found in class ObjectFactory");
    }
  }
  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class ObjectFactory");
    }
  }
}
