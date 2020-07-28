package com.WorldNavigator.Features;

import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.States.RoomStates.RoomAvailabilityStates.IRoomAvailabilityState;

public interface IObservable {
     void notifyChange(PlayerInfo player, IRoomAvailabilityState availabilityState);
     void addObserver(IObserver observer);
     void removeObserver(IObserver observer);

}
