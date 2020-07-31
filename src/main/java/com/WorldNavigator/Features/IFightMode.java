package com.WorldNavigator.Features;

import com.WorldNavigator.Commands.ICommand;

import java.util.List;

public interface IFightMode {
    public List<ICommand> getAvailableCommands();
    public void setPlayerChoice(String playerUsername, ICommand choice);
    public ICommand getPlayerChoice(String playerUsername);
    public List<String> getPlayersInFight();
    public void clearPlayerChoice();
    public boolean isReady();


}
