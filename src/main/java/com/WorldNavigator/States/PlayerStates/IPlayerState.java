package com.WorldNavigator.States.PlayerStates;

import com.WorldNavigator.Commands.ICommand;

import java.util.List;

public interface IPlayerState {

    List<ICommand> getAvailableCommands();

    String getName();
}
