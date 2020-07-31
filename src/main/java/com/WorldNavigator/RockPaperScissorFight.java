package com.WorldNavigator;

import com.WorldNavigator.Commands.ICommand;
import com.WorldNavigator.Commands.Paper;
import com.WorldNavigator.Commands.Rock;
import com.WorldNavigator.Commands.Scissors;
import com.WorldNavigator.Features.IFightMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RockPaperScissorFight implements IFightMode {
    Map<String, ICommand> playersChoice;
    boolean ready;


    public List<ICommand> getAvailableCommands(){
        List<ICommand> availableCommands=new ArrayList<>();
        availableCommands.add(new Rock());
        availableCommands.add(new Paper());
        availableCommands.add(new Scissors());
        return availableCommands;
    }

    public RockPaperScissorFight(){
        playersChoice = new HashMap<>();
        clearPlayerChoice();
        ready=false;
    }

    @Override
    public void setPlayerChoice(String playerUsername,ICommand choice){
        if(!playersChoice.containsKey(playerUsername)){
            playersChoice.put(playerUsername,choice);
        }
        if(playersChoice.size()==2){
            ready=true;
        }
    }

    @Override
    public ICommand getPlayerChoice(String playerUsername){
        if(!playersChoice.containsKey(playerUsername)){
            throw new IllegalStateException("Player choices should be set first before getting them.");
        }
        return playersChoice.get(playerUsername);
    }

    @Override
    public List<String> getPlayersInFight(){
        return playersChoice.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public void clearPlayerChoice(){
        playersChoice.clear();
    }

    @Override
    public boolean isReady(){
        return ready;
    }
}
