package com.WorldNavigator.controllers;

import com.WorldNavigator.services.PlayerService;
import com.WorldNavigator.utils.JSONDecode;
import com.WorldNavigator.reply.DefaultReply;
import com.WorldNavigator.reply.GameReply;
import com.WorldNavigator.reply.ListGamesReply;
import com.WorldNavigator.reply.MapFilesReply;
import com.WorldNavigator.requests.CommandRequest;
import com.WorldNavigator.requests.CreateRequest;
import com.WorldNavigator.requests.JoinRequest;
import com.WorldNavigator.requests.LoginRequest;
import com.WorldNavigator.services.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class GameController {

    GameService gameService;
    PlayerService playerService;

    @Autowired
     public GameController(GameService gameService, PlayerService playerService){
        this.gameService=gameService;
        this.playerService=playerService;
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

    @RequestMapping(value = "/api/game/unjoin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DefaultReply> unJoinGame(@RequestBody String jsonRequest) throws JsonProcessingException {
        DefaultReply reply = new DefaultReply();
        JoinRequest request = JSONDecode.decodeJsonString(jsonRequest,JoinRequest.class);
        String message = gameService.unJoinGame(request.getUsername(),request.getWorldName());
        reply.setValue(message);
        return new ResponseEntity(reply, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/game/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DefaultReply> createGame(@RequestBody String jsonRequest) throws IOException {
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
    public ResponseEntity<DefaultReply> startGame(@RequestBody String jsonRequest) throws JsonProcessingException {
        JoinRequest request = JSONDecode.decodeJsonString(jsonRequest,JoinRequest.class);
        DefaultReply reply = gameService.startGame(request.getUsername(),request.getWorldName());
        return new ResponseEntity(reply, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/game/list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ListGamesReply> getAvailableGames(@RequestBody String jsonRequest) throws JsonProcessingException {
        LoginRequest request = JSONDecode.decodeJsonString(jsonRequest,LoginRequest.class);
        ListGamesReply reply = new ListGamesReply();
        if(!playerService.isPlayerActive(request.getUsername())){
            return new ResponseEntity(reply, HttpStatus.OK);
        }
        reply.setAvailableGames(gameService.getAvailableGames());
        return new ResponseEntity(reply, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/game/map", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<MapFilesReply> getMapFiles(@RequestBody String jsonRequest) throws IOException {
        LoginRequest request = JSONDecode.decodeJsonString(jsonRequest,LoginRequest.class);
        MapFilesReply reply = new MapFilesReply();
        if(!playerService.isPlayerActive(request.getUsername())){
            return new ResponseEntity(reply, HttpStatus.OK);
        }
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:Maps/*");
        List<String> fileNames = new ArrayList<>();
        for(Resource r: resources) {
            fileNames.add(r.getFilename());
        }
        reply.mapFiles=fileNames;
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
