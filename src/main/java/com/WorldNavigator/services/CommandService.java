package com.WorldNavigator.services;

import com.WorldNavigator.commands.ICommand;
import com.WorldNavigator.states.playerStates.IPlayerState;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CommandService {
  public List<ICommand> getAvailableCommands(IPlayerState state) {
    return state.getAvailableCommands();
  }

  public ICommand getCommand(IPlayerState state, String command) {
    List<ICommand> availableCommands = getAvailableCommands(state);
    if (isNumberCommand(command)) {
      int commandIdx = Integer.parseInt(command);
      if (commandIdx >= 1 && commandIdx <= availableCommands.size()) {
        return availableCommands.get(commandIdx - 1);
      }
    }
    return availableCommands.stream()
        .filter(listCommand -> command.equals(listCommand.getName()))
        .findFirst()
        .orElse(null);
  }

  public List<String> splitCommand(String command) {
    return Arrays.asList(command.split(" "));
  }

  public boolean isNumberCommand(String command) {
    try {
      Integer.parseInt(command);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public String convertToString(List<ICommand> commands) {
    StringBuilder stringRepresentation = new StringBuilder();
    String prefix = "";
    int idx = 1;
    for (ICommand command : commands) {
      stringRepresentation.append(prefix);
      prefix = " | ";
      stringRepresentation.append("(").append(idx).append(") ").append(command.getName());
      idx++;
    }
    if (stringRepresentation.length() == 0) {
      return "No commands";
    }
    return stringRepresentation.toString();
  }
}
