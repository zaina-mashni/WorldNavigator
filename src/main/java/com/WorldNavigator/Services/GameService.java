package com.WorldNavigator.Services;

import com.WorldNavigator.Commands.ICommand;
import com.WorldNavigator.Direction;
import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Features.IObserver;
import com.WorldNavigator.GameControl;
import com.WorldNavigator.MapFileDecode;
import com.WorldNavigator.Messages.ErrorMessages;
import com.WorldNavigator.Messages.SuccessMessages;
import com.WorldNavigator.RPSFight;
import com.WorldNavigator.Reply.GameReply;
import com.WorldNavigator.States.PlayerStates.FightLevel;
import com.WorldNavigator.States.RoomStates.RoomAvailabilityStates.IRoomAvailabilityState;
import com.WorldNavigator.Views.GameInfoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameService implements IObserver {

    private SimpMessagingTemplate webSocket;

    CommandService commandService;
    PlayerService playerService;
    RPSFight rpsFight;
    private Map<String, GameControl> availableGames;

    @Autowired
    public GameService(
            PlayerService playerService,
            CommandService commandService,
            SimpMessagingTemplate webSocket) {
        this.playerService = playerService;
        this.commandService = commandService;
        this.webSocket = webSocket;
        availableGames =Collections.synchronizedMap( new HashMap<>());
        rpsFight=new RPSFight();
    }

    public String createNewGame(String adminName, String worldName, String mapFile)
            throws IOException {
        if (availableGames.containsKey(worldName)) {
            return ErrorMessages.gameExists;
        }
        if (playerService.isPlayerActive(adminName)) {
            availableGames.put(worldName, new MapFileDecode(worldName,playerService.getPlayer(adminName),this).setMapFile(mapFile).startDecode());
            joinGame(adminName,worldName);
            return SuccessMessages.createSuccess;
        }
        return ErrorMessages.userNotActive;
    }

    public String joinGame(String playerName, String worldName) {
        if (!availableGames.containsKey(worldName)) {
            return ErrorMessages.gameDoesNotExist;
        }
        if(!availableGames.get(worldName).isFull()) {
            if (playerService.isPlayerActive(playerName)) {
                playerService.playerJoinGame(playerName, worldName);
                availableGames.get(worldName).incrementJoinedPlayers();
                return SuccessMessages.joinSuccess;
            }
            return ErrorMessages.userNotActive;
        }
        return ErrorMessages.gameFull;
    }

    public GameReply setUpGameReply(PlayerInfo player){
        GameReply gameReply=new GameReply();
        gameReply.setAvailableCommands(commandService.convertToString(commandService.getAvailableCommands(player.getCurrentState())));
        gameReply.setTimeLeftInMinutes(availableGames.get(player.getWorldName()).getTimeLeft());
        gameReply.setFacingDirection(Direction.values()[player.getFacingDirection()].name());
        gameReply.setInventory(player.getInventory().toString());
        gameReply.setFighting(false);
        gameReply.setPlaying(true);
        return gameReply;
    }

    public GameReply setUpGameForQuitPlayer(PlayerInfo player){
        GameReply gameReply=new GameReply();
        gameReply.setAvailableCommands(commandService.convertToString(new ArrayList<ICommand>()));
        gameReply.setTimeLeftInMinutes(0);
        gameReply.setFacingDirection(Direction.values()[player.getFacingDirection()].name());
        gameReply.setInventory(player.getInventory().toString());
        gameReply.setFighting(false);
        gameReply.setPlaying(false);
        return gameReply;
    }

    public GameReply executeCommand(String username, String input) {
        if(playerService.getPlayer(username).getCurrentState().getName().equals("fightLevel")){
            return rPSFight(username,input);
        }
        PlayerInfo player = playerService.getPlayer(username);
        MapInfo map = availableGames.get(player.getWorldName()).getMap();
        List<String> splitCommand = commandService.splitCommand(input);
        ICommand command = commandService.getCommand(player.getCurrentState(), splitCommand.get(0));
        if (command == null) {
            GameReply gameReply=setUpGameReply(player);
            gameReply.setMessage( "Invalid command!");
            return gameReply;
        }
        String message= command.execute(player, map, splitCommand);
        if(player.getCurrentRoom().getStatusState().getName().equals("exit")){
            GameReply reply = setUpGameForQuitPlayer(player);
            reply.setMessage("You found your way out of the map!");
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                endGame(player.getWorldName(),username+" won! Game has ended");
            });
            thread.start();
            return reply;
        }
        GameReply gameReply=setUpGameReply(player);
        gameReply.setMessage(message);
        return gameReply;
    }

    public void startGame(String username, String worldName) {
        //check if admin
        GameControl game = availableGames.get(worldName);
        playerService
                .getPlayersInWorld(worldName)
                .forEach(
                        player -> webSocket.convertAndSend("/socket/start/" + player.getUsername(), startGameForPlayer(player, game)));
        game.setStarted(true);
        game.startTimer();
    }

    public GameReply startGameForPlayer(PlayerInfo player, GameControl game) {
        player.addToInventory(game.getInitialInventory().getItems());
        int spawnRoomIdx = (int) (Math.random() * game.getAvailableSpawnRooms().size());
        int facingDirection = (int) (Math.random() * 4);
        player.setCurrentRoom(game.assignSpawnRoom(spawnRoomIdx));
        player.getCurrentRoom().pushAvailabilityState(player);
        player.setFacingDirection(facingDirection);
        GameReply reply = setUpGameReply(player);
        reply.setMessage("The game has started!");
        return reply;
    }

    public GameReply startFightForPlayer(PlayerInfo player) {
        GameReply reply = setUpGameReply(player);
        reply.setMessage("You both have the same amount of money. Now you have to battle it out with a game of rock paper scissors! You have 7 seconds to execute a command, if you don't or you enter an invalid command, a random one will be chosen for you.");
        reply.setFighting(true);
        return reply;
    }

    public List<GameInfoView> getAvailableGames() {
        return availableGames.entrySet().stream().filter(game -> !game.getValue().isStarted()).map(game -> {
            return new GameInfoView(game.getValue().getWorldName(), game.getValue().getPlayerCapacity(), game.getValue().getPlayersJoined(), game.getValue().getAdmin().getUsername());
        }).collect(Collectors.toList());
    }

    @Override
    public void onNotify(PlayerInfo player,IRoomAvailabilityState availabilityState) {
        if(availabilityState.getName().equals("full")){
            PlayerInfo oppositePlayer=playerService.getOppositePlayerInRoom(player);
            GameReply reply= new GameReply();
            setUpGameReply(oppositePlayer);
            reply.setMessage(oppositePlayer.getCurrentRoom().handleAvailabilityStateSpecificInput(player,"forward"));
            webSocket.convertAndSend("/socket/start/" + oppositePlayer.getUsername(), reply);
            PlayerInfo winner=playerService.getRichestPlayer(playerService.getPlayersInSameRoomAsPlayer(player));
            if(winner==null){
                playerService.getPlayersInSameRoomAsPlayer(player).forEach( playerInfo -> {
                playerInfo.pushState(new FightLevel());
                webSocket.convertAndSend("/socket/start/" + playerInfo.getUsername(), startFightForPlayer(playerInfo));
            });
            } else{
                PlayerInfo loser = playerService.getOppositePlayerInRoom(winner);
                reply=setUpGameReply(winner);
                reply.setMessage("You have more money. You won!");
                reply.setFighting(false);
                webSocket.convertAndSend("/socket/start/" + winner.getUsername(), reply);
                reply=setUpGameReply(loser);
                reply.setMessage("You have less money. You lost!");
                reply.setFighting(false);
                reply.setPlaying(false);
                webSocket.convertAndSend("/socket/start/" + loser.getUsername(), reply);
                playerElimination(winner,loser);
            }
        }
    }

    public void playerElimination(PlayerInfo winner, PlayerInfo loser){
        playerService.distributeWealth(winner,loser);
        availableGames.get(winner.getWorldName()).eliminatePlayer(loser);
        playerService.getPlayersInWorld(winner.getWorldName()).stream().filter(player -> !player.getUsername().equals(loser.getUsername())).forEach(playerInfo -> {
            GameReply reply=setUpGameReply(playerInfo);
            reply.setMessage(loser.getUsername()+" has been eliminated! Some of their gold has been added to your inventory.");
            webSocket.convertAndSend("/socket/start/" + playerInfo.getUsername(), reply);
        });
    }

    public GameReply rPSFight(String playerUsername,String input) {
        PlayerInfo player = playerService.getPlayer(playerUsername);
        GameReply reply=setUpGameReply(player);
        List<String> splitCommand = commandService.splitCommand(input);
        ICommand command=commandService.getCommand(playerService.getPlayer(playerUsername).getCurrentState(),splitCommand.get(0));
        rpsFight.setPlayerChoice(playerUsername,command);
        if(rpsFight.isReady()){
            PlayerInfo oppositePlayer=playerService.getOppositePlayerInRoom(playerService.getPlayer(playerUsername));
            splitCommand = Collections.singletonList(rpsFight.getPlayerChoice(oppositePlayer.getUsername()).getName());
            String result=command.execute(playerService.getPlayer(playerUsername),availableGames.get(playerService.getPlayer(playerUsername).getWorldName()).getMap(),splitCommand);
            splitCommand = Collections.singletonList(command.getName());
            String oppositeResult = rpsFight.getPlayerChoice(oppositePlayer.getUsername()).execute(oppositePlayer,availableGames.get(oppositePlayer.getWorldName()).getMap(),splitCommand);
            boolean isTie = true;
            boolean isWinning = false;
            if(result.matches(".*lost.*")){
                player.popState();
                oppositePlayer.popState();
                Thread thread = new Thread(() -> playerElimination(oppositePlayer,player));
                thread.start();
                isTie=false;
            }
            else if(result.matches(".*won.*")){
                player.popState();
                oppositePlayer.popState();
                Thread thread = new Thread(() -> playerElimination(player,oppositePlayer));
                thread.start();
                isTie=false;
                isWinning=true;
            }
            reply = setUpGameReply(oppositePlayer);
            reply.setMessage(oppositeResult);
            if(isTie) {
                reply.setFighting(true);
            }
            if(isWinning){
                reply.setPlaying(false);
            }
            webSocket.convertAndSend("/socket/start/" + oppositePlayer.getUsername(), reply);
            reply = setUpGameReply(player);
            reply.setMessage(result);
            if(isTie){
                reply.setFighting(true);
            }
            if(!isTie && !isWinning){
                reply.setPlaying(false);
            }
            rpsFight.clearPlayerChoice();
            return reply;
        }
        reply.setMessage("Waiting for the other player to choose...");
        reply.setFighting(true);
        reply.setPlaying(true);
        return reply;
    }

    public GameReply quitPlayer(String username, String worldName) {
        if(availableGames.get(worldName).getPlayersJoined() == 1){
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                endGame( worldName,"Game ended!");
            });
            thread.start();
            GameReply reply = setUpGameForQuitPlayer(playerService.getPlayer(username));
            reply.setMessage("You quit the game!");
            return reply;
        }
        PlayerInfo player=playerService.getPlayer(username);
        playerService.distributeGold(player);
        availableGames.get(worldName).createChest(player);
        availableGames.get(worldName).eliminatePlayer(player);
        playerService.getPlayersInWorld(worldName).stream().filter(playerInfo -> !playerInfo.getUsername().equals(player.getUsername())).forEach(playerInfo -> {
            GameReply reply=setUpGameReply(playerInfo);
            reply.setMessage(username+" has quit! Some of their gold has been added to your inventory.");
            webSocket.convertAndSend("/socket/start/" + playerInfo.getUsername(), reply);
        });
        GameReply reply = setUpGameForQuitPlayer(player);
        reply.setMessage("You quit the game!");
        return reply;
    }

    public void endGame(String worldName, String message) {
        playerService.getPlayersInWorld(worldName).forEach(player -> {
            GameReply reply = setUpGameForQuitPlayer(player);
            reply.setMessage(message);
            webSocket.convertAndSend("/socket/start/" + player.getUsername(), reply);
        });
        availableGames.remove(worldName);
    }
}
