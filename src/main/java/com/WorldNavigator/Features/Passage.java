package com.WorldNavigator.Features;

import com.WorldNavigator.Entities.Room;

public class Passage implements IFeature {
    private Room firstRoom;
    private Room secondRoom;
    int firstRoomWallIdx;
    int secondRoomWallIdx;
    private static final String NAME="passage";

    public Passage(Room firstRoom,int firstRoomWallIdx,Room secondRoom,int secondRoomWallIdx){
        this.firstRoom=firstRoom;
        this.secondRoom=secondRoom;
        this.firstRoomWallIdx=firstRoomWallIdx;
        this.secondRoomWallIdx=secondRoomWallIdx;
    }

    public Room getOppositeRoom(Room room){
        if(firstRoom==room)
            return secondRoom;
        return firstRoom;
    }

    public int getOppositeWallIdx(int wallIdx){
        if(firstRoomWallIdx==wallIdx)
            return secondRoomWallIdx;
        return firstRoomWallIdx;
    }

    public String getFeatureName(){
        return NAME;
    }
}
