package com.WorldNavigator.Entities;

import com.WorldNavigator.Features.Passage;

import java.util.ArrayList;
import java.util.List;

public class MapInfo {
    private List<Room> rooms;

    public MapInfo( List<Room> rooms) {
        this.rooms = rooms;
    }

    public Room getNeighboringRoom(Object object, Room currentRoom) {
        if(!object.hasFeature("passage")){
            throw new IllegalArgumentException("object passed does not connect two rooms in MapInfo.getNeighboringRoom");
        }
        return ((Passage) object.getFeature("passage")).getOppositeRoom(currentRoom);
    }

    public Room getRoom(int roomIndex) {
        return rooms.get(roomIndex);
    }

}
