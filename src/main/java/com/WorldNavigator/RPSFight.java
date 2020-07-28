package com.WorldNavigator;

import com.WorldNavigator.Commands.ICommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RPSFight {
    Map<String, ICommand> playersChoice;
    boolean ready;

    public RPSFight(){
        playersChoice = new HashMap<>();
        clearPlayerChoice();
        ready=false;
    }

    public void setPlayerChoice(String playerUsername,ICommand choice){
        if(!playersChoice.containsKey(playerUsername)){
            playersChoice.put(playerUsername,choice);
        }
        if(playersChoice.size()==2){
            ready=true;
        }
    }

    public ICommand getPlayerChoice(String playerUsername){
        if(!playersChoice.containsKey(playerUsername)){
            throw new IllegalStateException("Player choices should be set first before getting them.");
        }
        return playersChoice.get(playerUsername);
    }

    public List<String> getPlayersInFight(){
        return playersChoice.keySet().stream().collect(Collectors.toList());
    }

    public void clearPlayerChoice(){
        playersChoice.clear();
    }

    public boolean isReady(){
        return ready;
    }
}
