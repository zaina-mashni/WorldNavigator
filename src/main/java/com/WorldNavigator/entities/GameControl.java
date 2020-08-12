package com.WorldNavigator.entities;

import com.WorldNavigator.entities.*;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.factories.ObjectFactory;
import com.WorldNavigator.features.Container;
import com.WorldNavigator.services.GameService;

import java.util.ArrayList;
import java.util.List;

public class GameControl {

  private String worldName;
  private MapInfo map;
  private PlayerInfo admin;
  private Container initialInventory;
  private int playerCapacity;
  private List<Integer> availableSpawnRooms;
  private boolean started;
  private int playersJoined;
  private int timeRemaining;
  private GameService gameService;

  public void setTimeRemaining(int timeRemaining) {
    this.timeRemaining = timeRemaining;
  }

  public GameControl(String worldName, PlayerInfo admin, GameService gameService) {
    this.worldName = worldName;
    availableSpawnRooms = new ArrayList<>();
    this.gameService = gameService;
    started = false;
    playersJoined = 0;
    this.admin = admin;
  }

  public String getWorldName() {
    return worldName;
  }

  public void setInitialInventory(Container initialInventory) {
    this.initialInventory = initialInventory;
  }

  public void setAvailableSpawnRooms(List<Integer> availableSpawnRooms) {
    this.availableSpawnRooms = availableSpawnRooms;
  }

  public void setMap(List<Room> rooms) {
    this.map = new MapInfo(rooms);
  }

  public void setPlayerCapacity(int playerCapacity) {
    this.playerCapacity = playerCapacity;
  }

  public MapInfo getMap() {
    return map;
  }

  public Container getInitialInventory() {
    return initialInventory;
  }

  public int getPlayerCapacity() {
    return playerCapacity;
  }

  public int getTimeRemaining() {
    return timeRemaining;
  }

  public void setStarted(boolean started) {
    this.started = started;
  }

  public boolean isStarted() {
    return started;
  }

  public void incrementJoinedPlayers() {
    if (isFull()) throw new IllegalStateException("game is full.");
    playersJoined++;
  }

  public boolean isFull() {
    return playersJoined == playerCapacity;
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
    Room room = map.getRoom(availableSpawnRooms.get(spawnRoomIdx));
    availableSpawnRooms.remove(spawnRoomIdx);
    return room;
  }

  public void eliminatePlayer(PlayerInfo player) {
    player.getCurrentRoom().popAvailabilityState();
    player.quitWorld();
    decrementJoinedPlayers();
  }

  public void createChest(PlayerInfo player) {
    Wall wall = player.getCurrentRoom().getWall(player.getFacingDirection());
    Object chest = new ObjectFactory().buildObject(player.getUsername() + "Chest", "opened");
    Container container = new Container();
    container.addItems(player.getInventory().getItems());
    player.getInventory().removeAll();
    chest.addOrReplaceFeature(container);
    wall.addOrReplaceObject(chest);
  }

  public void decrementJoinedPlayers() {
    if (playersJoined == 1 && !isStarted()) {
      throw new IllegalStateException("Admin can not leave game before it started.");
    }
    playersJoined--;
    if (playersJoined == 0) {
      Thread thread =
          new Thread(
              () -> {
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                gameService.endGame(worldName, "Game ended!","OK");
              });
      thread.start();
    }
  }
}
