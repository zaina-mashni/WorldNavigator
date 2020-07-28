package com.WorldNavigator.Controllers;

import com.WorldNavigator.JSONDecode;
import com.WorldNavigator.Reply.DefaultReply;
import com.WorldNavigator.Reply.GameReply;
import com.WorldNavigator.Reply.ListGamesReply;
import com.WorldNavigator.Reply.MapFilesReply;
import com.WorldNavigator.Requests.CommandRequest;
import com.WorldNavigator.Requests.CreateRequest;
import com.WorldNavigator.Requests.JoinRequest;
import com.WorldNavigator.Requests.LoginRequest;
import com.WorldNavigator.Services.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class GameController {

    GameService gameService;

    @Autowired
     public GameController(GameService gameService){
        this.gameService=gameService;
    }

    @RequestMapping(value = "/api/game/join", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DefaultReply> joinGame(@RequestBody String jsonRequest) throws JsonProcessingException {
        DefaultReply reply = new DefaultReply();
        JoinRequest request = JSONDecode.decodeJsonString(jsonRequest,JoinRequest.class);
        String message = gameService.joinGame(request.getUsername(),request.getWorldName());
        reply.setValue(message);
        return new ResponseEntity(reply, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/game/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DefaultReply> createGame(@RequestBody String jsonRequest) throws JsonProcessingException, FileNotFoundException {
        DefaultReply reply = new DefaultReply();
        CreateRequest request = JSONDecode.decodeJsonString(jsonRequest,CreateRequest.class);
        String message = gameService.createNewGame(request.getUsername(),request.getWorldName(),request.getMapFile());
        reply.setValue(message);
        return new ResponseEntity(reply, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/game/command", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DefaultReply> executeCommand(@RequestBody String jsonRequest) throws JsonProcessingException {
        CommandRequest request = JSONDecode.decodeJsonString(jsonRequest,CommandRequest.class);
        GameReply reply = gameService.executeCommand(request.getUsername(),request.getCommand());
        return new ResponseEntity(reply, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/game/start", method = RequestMethod.POST)
    @ResponseBody
    public void startGame(@RequestBody String jsonRequest) throws JsonProcessingException {
        JoinRequest request = JSONDecode.decodeJsonString(jsonRequest,JoinRequest.class);
        gameService.startGame(request.getUsername(),request.getWorldName());
    }

    @RequestMapping(value = "/api/game/list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ListGamesReply> getAvailableGames(@RequestBody String jsonRequest) throws JsonProcessingException {
        LoginRequest request = JSONDecode.decodeJsonString(jsonRequest,LoginRequest.class);
        //check if logged in
        ListGamesReply reply = new ListGamesReply();
        reply.setAvailableGames(gameService.getAvailableGames());
        return new ResponseEntity(reply, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/game/map", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<MapFilesReply> getMapFiles(@RequestBody String jsonRequest) throws IOException {
        LoginRequest request = JSONDecode.decodeJsonString(jsonRequest,LoginRequest.class);
        //check if logged in
        MapFilesReply reply = new MapFilesReply();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (
                final InputStream is = loader.getResourceAsStream("Maps");
                final InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                final BufferedReader br = new BufferedReader(isr)) {
            reply.mapFiles = br.lines()
                    .map(l -> "Maps" + "/" + l)
                    .map(r -> loader.getResource(r)).map(f -> f.toString())
                    .collect(Collectors.toList());
        }
        URL url = loader.getResource("Maps");
        String path = url.getPath();
        reply.mapFiles= Arrays.stream(Objects.requireNonNull(new File(path).listFiles())).map(File::getName).collect(Collectors.toList());
        return new ResponseEntity(reply, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/game/quit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<GameReply> quitGame(@RequestBody String jsonRequest) throws JsonProcessingException {
        JoinRequest request = JSONDecode.decodeJsonString(jsonRequest,JoinRequest.class);
        GameReply reply = gameService.quitPlayer(request.getUsername(),request.getWorldName());
        return new ResponseEntity(reply, HttpStatus.OK);
    }



}
