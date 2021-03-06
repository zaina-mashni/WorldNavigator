package com.WorldNavigator.controllers;

import com.WorldNavigator.utils.JSONDecode;
import com.WorldNavigator.reply.DefaultReply;
import com.WorldNavigator.requests.LoginRequest;
import com.WorldNavigator.services.PlayerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AuthenticationController {

  PlayerService playerService;

  @Autowired
  public AuthenticationController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @RequestMapping(value = "/api/auth/login", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<DefaultReply> login(@RequestBody String jsonRequest)
      throws JsonProcessingException {
    LoginRequest request = JSONDecode.decodeJsonString(jsonRequest, LoginRequest.class);
    DefaultReply reply = playerService.playerLogin(request.getUsername(), request.getPassword());
    return new ResponseEntity(reply, HttpStatus.OK);
  }

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @RequestMapping(value = "/api/auth/register", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<DefaultReply> register(@RequestBody String jsonRequest)
      throws JsonProcessingException {
    LoginRequest request = JSONDecode.decodeJsonString(jsonRequest, LoginRequest.class);
    DefaultReply reply = playerService.registerPlayer(request.getUsername(), request.getPassword());
    return new ResponseEntity(reply, HttpStatus.OK);
  }

  @RequestMapping(value = "/api/auth/logout", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<DefaultReply> logout(@RequestBody String jsonRequest)
      throws JsonProcessingException {
    LoginRequest request = JSONDecode.decodeJsonString(jsonRequest, LoginRequest.class);
    DefaultReply reply = playerService.playerLogout(request.getUsername());
    return new ResponseEntity(reply, HttpStatus.OK);
  }
}
