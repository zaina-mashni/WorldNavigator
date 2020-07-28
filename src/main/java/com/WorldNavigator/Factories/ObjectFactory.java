package com.WorldNavigator.Factories;

import com.WorldNavigator.Behaviours.ObjectBehaviour;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.States.ObjectStates.*;
import org.springframework.stereotype.Component;

@Component
public class ObjectFactory {
    public Object buildObject(String objectName, String state){
        switch (state){
            case "static":
                return new Object(objectName,new ObjectBehaviour(new Static()));
            case "locked":
                return new Object(objectName,new ObjectBehaviour(new Locked()));
            case "unlocked":
                return new Object(objectName,new ObjectBehaviour(new Unlocked()));
            case "opened":
                return new Object(objectName,new ObjectBehaviour(new Opened()));
            case "closed":
                return new Object(objectName,new ObjectBehaviour(new Closed()));
            default:
                throw new IllegalArgumentException("state is not found in ItemFactory.buildItem");
        }
    }
}
