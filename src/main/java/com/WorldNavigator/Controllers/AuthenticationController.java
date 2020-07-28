package com.WorldNavigator.Controllers;

import com.WorldNavigator.JSONDecode;
import com.WorldNavigator.Reply.DefaultReply;
import com.WorldNavigator.Requests.LoginRequest;
import com.WorldNavigator.Services.PlayerService;
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
    public AuthenticationController(PlayerService playerService){
        this.playerService=playerService;
    }

    @RequestMapping(value = "/api/auth/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DefaultReply> login(@RequestBody String jsonRequest) throws JsonProcessingException {
        DefaultReply reply = new DefaultReply();
        LoginRequest request = JSONDecode.decodeJsonString(jsonRequest,LoginRequest.class);
        String message = playerService.playerLogin(request.getUsername(),request.getPassword());
        reply.setValue(message);
        return new ResponseEntity(reply, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/api/auth/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DefaultReply> register(@RequestBody String jsonRequest) throws JsonProcessingException {
        DefaultReply reply = new DefaultReply();
        LoginRequest request = JSONDecode.decodeJsonString(jsonRequest,LoginRequest.class);
        String message = playerService.registerPlayer(request.getUsername(),request.getPassword());
        reply.setValue(message);
        return new ResponseEntity(reply, HttpStatus.OK);
    }


}
