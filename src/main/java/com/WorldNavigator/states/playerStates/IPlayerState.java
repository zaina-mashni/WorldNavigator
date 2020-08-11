package com.WorldNavigator.states.playerStates;

import com.WorldNavigator.commands.ICommand;

import java.util.List;

public interface IPlayerState {

    List<ICommand> getAvailableCommands();

    String getName();
}
