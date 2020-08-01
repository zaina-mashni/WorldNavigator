package com.WorldNavigator.Entities;

import com.WorldNavigator.Features.IObservable;
import com.WorldNavigator.Features.IObserver;
import com.WorldNavigator.Services.GameService;
import com.WorldNavigator.States.RoomStates.RoomAvailabilityStates.Empty;
import com.WorldNavigator.States.RoomStates.RoomAvailabilityStates.Full;
import com.WorldNavigator.States.RoomStates.RoomAvailabilityStates.IRoomAvailabilityState;
import com.WorldNavigator.States.RoomStates.RoomAvailabilityStates.Open;
import com.WorldNavigator.States.RoomStates.RoomStatusStates.IRoomStatusState;

import java.util.*;

public class Room implements IObservable {

    private int roomIndex;
    private List<Wall> walls;
    private Stack<IRoomAvailabilityState> availabilityStates;
    private IRoomStatusState statusState;
    private List<IObserver> observers;
    private int roomIdx;

    public Room(IRoomStatusState statusState,GameService gameService,int roomIndex) {
        walls = new ArrayList<>(Arrays.asList(new Wall(), new Wall(), new Wall(), new Wall()));
        availabilityStates= new Stack<>();
        availabilityStates.push(new Empty());
        this.statusState=statusState;
        observers=new ArrayList<>();
        observers.add(gameService);
        this.roomIdx=roomIndex;
    }

    public void setStatusState(IRoomStatusState statusState) {
        this.statusState = statusState;
    }

    public void pushAvailabilityState(PlayerInfo player){
        if(getAvailabilityState().getName().equals("empty")){
            this.availabilityStates.push(new Open());
        }
        else if(getAvailabilityState().getName().equals("open")){
            this.availabilityStates.push(new Full());
        }
        if(availabilityStates.peek().getName().equals("full")){
            Thread thread = new Thread(() -> notifyChange(player,availabilityStates.peek()));
            thread.start();
        }
    }

    public void popAvailabilityState(){
        this.availabilityStates.pop();
    }

    public IRoomStatusState getStatusState() {
        return statusState;
    }

    public IRoomAvailabilityState getAvailabilityState() {
        return availabilityStates.peek();
    }

    public Wall getWall(int wallIndex) {
        checkWallIndex(wallIndex);
        return walls.get(wallIndex);
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    public void handleStatusStateChangeInput(PlayerInfo player,String input){
        statusState=statusState.handleStateChangeInput(player,input);
    }


    public String handleStatusStateSpecificInput(PlayerInfo player,String input){
        return statusState.handleStateSpecificInput(player,input);
    }


    public String handleAvailabilityStateSpecificInput(PlayerInfo player,String input){
        return availabilityStates.peek().handleStateSpecificInput(player,input);
    }

    public void addToWall(int wallIdx,Object object){
        walls.get(wallIdx).addObject(object);
    }

    public void setRoomIndex(int index) {
        if (index < 0) throw new IndexOutOfBoundsException("index "+index+" of room can not be less than 0 in Room.setRoomIndex.");
        this.roomIndex = index;
    }

    private void checkWallIndex(int index) {
        if (index < 0 || index > 4) {
            throw new IndexOutOfBoundsException("wall index "+index+" can not be less than 0 or greater than 4 in Room.checkWallIndex.");
        }
    }

    @Override
    public void notifyChange(PlayerInfo player,IRoomAvailabilityState availabilityState) {
        for (IObserver observer: observers) {
            observer.onNotify(player,availabilityState);
        }
    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

}
