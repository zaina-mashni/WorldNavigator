package com.WorldNavigator.factories;

import com.WorldNavigator.entities.Room;
import com.WorldNavigator.exceptions.MissingStateInFactory;
import com.WorldNavigator.services.GameService;
import com.WorldNavigator.states.roomStates.roomStatusStates.Dark;
import com.WorldNavigator.states.roomStates.roomStatusStates.ExitRoom;
import com.WorldNavigator.states.roomStates.roomStatusStates.Lit;
import com.WorldNavigator.states.roomStates.roomStatusStates.NoSwitch;
import org.springframework.stereotype.Component;

@Component
public class RoomFactory {
    public Room buildRoom(String state, GameService gameService,int roomIdx) {
        checkIfNull("State",state);
        checkIfNull("GameService",gameService);
        switch (state){
            case "noSwitch":
                return new Room(new NoSwitch(),gameService,roomIdx);
            case "dark":
                return new Room(new Dark(),gameService,roomIdx);
            case "lit":
                return new Room(new Lit(),gameService,roomIdx);
            case "exit":
                return new Room(new ExitRoom(),gameService,roomIdx);
            default:
                throw new MissingStateInFactory(state+" state is not found in class RoomFactory");
        }
    }
    private void checkIfNull(String key, java.lang.Object value) {
        if (value == null) {
            throw new IllegalArgumentException(key + " can not be null in class RoomFactory");
        }
    }
}
