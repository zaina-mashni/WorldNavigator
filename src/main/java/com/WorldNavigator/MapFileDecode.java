package com.WorldNavigator;

import com.WorldNavigator.Entities.Item;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Entities.Room;
import com.WorldNavigator.Factories.ItemFactory;
import com.WorldNavigator.Factories.ObjectFactory;
import com.WorldNavigator.Factories.RoomFactory;
import com.WorldNavigator.Features.*;
import com.WorldNavigator.Services.GameService;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MapFileDecode {
    ItemFactory itemFactory;
    RoomFactory roomFactory;
    ObjectFactory objectFactory;
    GameControl gameControl;
    GameService gameService;
    Scanner scanner;
    List<Room> rooms;

    public MapFileDecode(String worldName, PlayerInfo admin, GameService gameService) {
        this.itemFactory = new ItemFactory();
        this.roomFactory = new RoomFactory();
        this.objectFactory = new ObjectFactory();
        this.gameService=gameService;
        gameControl = new GameControl(worldName,admin);
    }

    public MapFileDecode setMapFile(String mapFile) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream is = loader.getResourceAsStream("Maps/"+mapFile);
        scanner = new Scanner(is);
        return this;
    }

    public GameControl startDecode() {
        int time = scanner.nextInt();
        gameControl.setTimer(time);
        gameControl.getTimer().setGameService(gameService);
        playerInfoDecode();
        roomDecode();
        objectDecode();
        gameControl.setMap(rooms);
        return gameControl;
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
            object.addFeature(feature);
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
            Room room = roomFactory.buildRoom(roomStatus,gameService,i);
            rooms.add(room);
        }
    }
}
