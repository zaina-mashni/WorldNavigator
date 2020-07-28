package com.WorldNavigator.Factories;

import com.WorldNavigator.Entities.Room;
import com.WorldNavigator.Services.GameService;
import com.WorldNavigator.States.RoomStates.RoomStatusStates.Dark;
import com.WorldNavigator.States.RoomStates.RoomStatusStates.ExitRoom;
import com.WorldNavigator.States.RoomStates.RoomStatusStates.Lit;
import com.WorldNavigator.States.RoomStates.RoomStatusStates.NoSwitch;
import org.springframework.stereotype.Component;

@Component
public class RoomFactory {
    public Room buildRoom(String state, GameService gameService,int roomIdx){
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
                throw new IllegalArgumentException("state is not found in RoomFactory.buildRoom");
        }
    }
}
