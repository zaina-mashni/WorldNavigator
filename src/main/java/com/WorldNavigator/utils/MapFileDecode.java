package com.WorldNavigator.utils;

import com.WorldNavigator.Direction;
import com.WorldNavigator.entities.Item;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.entities.Room;
import com.WorldNavigator.exceptions.ErrorInInputFile;
import com.WorldNavigator.factories.ItemFactory;
import com.WorldNavigator.factories.ObjectFactory;
import com.WorldNavigator.factories.RoomFactory;
import com.WorldNavigator.features.*;
import com.WorldNavigator.GameControl;
import com.WorldNavigator.services.GameService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MapFileDecode {
  private ItemFactory itemFactory;
  private RoomFactory roomFactory;
  private ObjectFactory objectFactory;
  private GameControl gameControl;
  private GameService gameService;
  private Scanner scanner;
  private List<Room> rooms;

  public MapFileDecode(String worldName, PlayerInfo admin, GameService gameService) {
    checkIfNull("WorldName", worldName);
    checkIfNull("Admin", admin);
    checkIfNull("GameService", gameService);
    this.itemFactory = new ItemFactory();
    this.roomFactory = new RoomFactory();
    this.objectFactory = new ObjectFactory();
    this.gameService = gameService;
    gameControl = new GameControl(worldName, admin, gameService);
  }

  public MapFileDecode setMapFile(String mapFileName) throws IOException {
    checkIfNull("MapFileName", mapFileName);
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Resource[] resources = resolver.getResources("classpath*:Maps/" + mapFileName);
    if (resources.length != 1) {
      throw new IllegalArgumentException("map file not in Maps directory in class MapFileDecode.");
    }
    System.out.println("Reading file from resources");
    scanner = new Scanner(resources[0].getInputStream());
    System.out.println("Finished reading");
    return this;
  }

  public GameControl startDecode() {
    try{
      int time = scanner.nextInt();
      gameControl.setTimeRemaining(time);
      playerInfoDecode();
      roomDecode();
      objectDecode();
      gameControl.setMap(rooms);
      return gameControl;
    }
    catch (Exception e){
      throw new ErrorInInputFile(e.getMessage());
    }
  }

  private void playerInfoDecode() {
    int noOfPlayers = scanner.nextInt();
    List<Integer> availableSpawnRooms = new ArrayList<>(noOfPlayers);
    for (int i = 0; i < noOfPlayers; ++i) {
      int roomIdx = scanner.nextInt();
      availableSpawnRooms.add(roomIdx);
    }
    gameControl.setPlayerCapacity(noOfPlayers);
    gameControl.setAvailableSpawnRooms(availableSpawnRooms);
    int noOfInventoryItems = scanner.nextInt();
    Container inventory = new Container();
    for (int i = 0; i < noOfInventoryItems; ++i) {
      Item item = itemDecode();
      int itemAmount = scanner.nextInt();
      inventory.addItem(item, itemAmount);
    }
    gameControl.setInitialInventory(inventory);
  }

  private void roomDecode() {
    int noOfRooms = scanner.nextInt();
    rooms = new ArrayList<>(noOfRooms);
    for (int i = 0; i < noOfRooms; i++) {
      String roomStatus = scanner.next();
      Room room = roomFactory.buildRoom(roomStatus, gameService, i);
      rooms.add(room);
    }
  }

  private void objectDecode() {
    int noOfObjects = scanner.nextInt();
    for (int i = 0; i < noOfObjects; i++) {
      String objectName = scanner.next();
      int roomIdx = scanner.nextInt();
      int wallIdx = scanner.nextInt();
      String state = scanner.next();
      Object object = objectFactory.buildObject(objectName, state);
      featureDecode(object, roomIdx, wallIdx);
      rooms.get(roomIdx).addToWall(wallIdx, object);
    }
  }

  private void featureDecode(Object object, int firstRoomIdx, int firstRoomWallIdx) {
    checkIfNull("Object", object);
    if (firstRoomIdx < 0 || firstRoomIdx >= rooms.size()) {
      throw new IndexOutOfBoundsException(
          "FirstRoomIdx should be between 0 and " + (rooms.size() - 1) + " in class MapFileDecode");
    }
    if (firstRoomWallIdx < 0 || firstRoomWallIdx >= Direction.values().length) {
      throw new IndexOutOfBoundsException(
          "FirstRoomWallIdx should be between 0 and "
              + (Direction.values().length - 1)
              + " in class MapFileDecode");
    }
    int noOfFeatures = scanner.nextInt();
    for (int i = 0; i < noOfFeatures; i++) {
      String featureName = scanner.next();
      IFeature feature;
      switch (featureName) {
        case "passage":
          feature = passageFeatureDecode(firstRoomIdx, firstRoomWallIdx);
          break;
        case "container":
          feature = containerFeatureDecode();
          break;
        case "lock":
          feature = lockFeatureDecode();
          break;
        case "trade":
          feature = tradeFeatureDecode();
          break;
        default:
          throw new IllegalArgumentException("error in input file"); // add custom exception
      }
      object.addOrReplaceFeature(feature);
    }
    if (object.hasFeature("passage")) {
      Room secondRoom =
          ((Passage) object.getFeature("passage")).getOppositeRoom(rooms.get(firstRoomIdx));
      int secondWallIdx =
          ((Passage) object.getFeature("passage")).getOppositeWallIdx(firstRoomWallIdx);
      secondRoom.addToWall(secondWallIdx, object);
    }
  }

  private Trade tradeFeatureDecode() {
    return new Trade(containerFeatureDecode());
  }

  private Lock lockFeatureDecode() {
    return new Lock(itemDecode());
  }

  private Container containerFeatureDecode() {
    Container container = new Container();
    int noOfItems = scanner.nextInt();
    for (int i = 0; i < noOfItems; i++) {
      Item item = itemDecode();
      int itemAmount = scanner.nextInt();
      container.addItem(item, itemAmount);
    }
    return container;
  }

  private Passage passageFeatureDecode(int firstRoomIdx, int firstRoomWallIdx) {
    if (firstRoomIdx < 0 || firstRoomIdx >= rooms.size()) {
      throw new IndexOutOfBoundsException(
          "FirstRoomIdx should be between 0 and " + (rooms.size() - 1) + " in class MapFileDecode");
    }
    if (firstRoomWallIdx < 0 || firstRoomWallIdx >= Direction.values().length) {
      throw new IndexOutOfBoundsException(
          "FirstRoomWallIdx should be between 0 and "
              + (Direction.values().length - 1)
              + " in class MapFileDecode");
    }
    int secondRoomIdx = scanner.nextInt();
    int secondRoomWallIdx = scanner.nextInt();
    return new Passage(
        rooms.get(firstRoomIdx), firstRoomWallIdx, rooms.get(secondRoomIdx), secondRoomWallIdx);
  }

  private Item itemDecode() {
    String itemName = scanner.next();
    String state = scanner.next();
    int cost = scanner.nextInt();
    return itemFactory.buildItem(itemName, state, cost);
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class MapFileDecode");
    }
  }
}
