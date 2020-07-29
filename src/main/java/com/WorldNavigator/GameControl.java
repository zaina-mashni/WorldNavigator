package com.WorldNavigator;

import com.WorldNavigator.Entities.*;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Factories.ObjectFactory;
import com.WorldNavigator.Features.Container;
import com.WorldNavigator.Services.GameService;
import com.WorldNavigator.States.ObjectStates.Opened;
import com.WorldNavigator.States.RoomStates.RoomAvailabilityStates.Open;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameControl {

    private String worldName;
    private GameTimer timer;
    private MapInfo map;
    private PlayerInfo admin;
    private Container initialInventory;
    private int playerCapacity;
    private List<Integer> availableSpawnRooms;
    private boolean started;
    private int playersJoined;

    public GameControl(String worldName,PlayerInfo admin){
        this.worldName=worldName;
        availableSpawnRooms=new ArrayList<>();
        timer=new GameTimer(worldName);
        started=false;
        playersJoined=0;
        this.admin=admin;
    }

    public GameTimer getTimer(){
        return timer;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setInitialInventory(Container initialInventory) {
        this.initialInventory = initialInventory;
    }

    public void setAvailableSpawnRooms(List<Integer> availableSpawnRooms){
        this.availableSpawnRooms=availableSpawnRooms;
    }

    public void setMap(List<Room> rooms) {
        this.map = new MapInfo(rooms);
    }

    public void setPlayerCapacity(int playerCapacity) {
        this.playerCapacity = playerCapacity;
    }

    public void setTimer(int time){
        long timeInSecond=((long)time)*60;
        timer.setFinishTime(timeInSecond);
    }

    public MapInfo getMap(){
        return map;
    }

    public Container getInitialInventory() {
        return initialInventory;
    }

    public int getPlayerCapacity() {
        return playerCapacity;
    }

    public int getTimeLeft() {
        return (int) (timer.getFinishTime()/60);
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    public void startTimer() {
        timer.schedule();
    }

    public void incrementJoinedPlayers(){
        if(isFull())
            throw new IllegalStateException("game is full.");
        playersJoined++;
    }

    public boolean isFull() {
        return playersJoined==playerCapacity;
    }

    public int getPlayersJoined() {
        return playersJoined;
    }

    public PlayerInfo getAdmin() {
        return admin;
    }


    public List<Integer> getAvailableSpawnRooms() {
        return availableSpawnRooms;
    }

    public Room assignSpawnRoom(int spawnRoomIdx) {
        Room room=map.getRoom(availableSpawnRooms.get(spawnRoomIdx));
        availableSpawnRooms.remove(spawnRoomIdx);
        return room;
    }

    public void eliminatePlayer(PlayerInfo player){
        player.setWorld("");
        playersJoined--;
    }

    public void createChest(PlayerInfo player){
        Wall wall = player.getCurrentRoom().getWall(player.getFacingDirection());
        Object chest = new ObjectFactory().buildObject(player.getUsername()+"Chest", "opened");
        Container container = new Container();
        container.addItems(player.getInventory().getItems());
        player.getInventory().removeAll();
        chest.addFeature(new Container());
        wall.addObject(chest);
    }
}
