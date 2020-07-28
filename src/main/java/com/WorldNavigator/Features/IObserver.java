package com.WorldNavigator.Features;

import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.States.RoomStates.RoomAvailabilityStates.IRoomAvailabilityState;

public interface IObserver {
    void onNotify(PlayerInfo player,IRoomAvailabilityState availabilityState);
}
