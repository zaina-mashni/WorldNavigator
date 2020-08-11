package com.WorldNavigator.services;

import com.WorldNavigator.entities.GameTimer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TimerService {
  Map<String, GameTimer> gameTimers;

  public TimerService() {
    gameTimers = new HashMap<>();
  }

  public void startTimerForGame(String worldName, int timeInMinutes, GameService gameService) {
    checkIfNull("WorldName", worldName);
    checkIfNull("GameService", gameService);
    if (timeInMinutes <= 0) {
      throw new IllegalArgumentException(
          "Time can not be less than or equal to zero in class TimerService");
    }
    if (containsTimerForGame(worldName)) {
      throw new IllegalArgumentException(
          "You can not start timer for a game after it started in class TimerService");
    }
    long timeInSeconds = ((long) timeInMinutes) * 60;
    GameTimer timer = new GameTimer(worldName);
    timer.setGameService(gameService);
    timer.schedule(timeInSeconds);
    gameTimers.put(worldName, timer);
  }

  public boolean containsTimerForGame(String worldName) {
    checkIfNull("WorldName", worldName);
    return gameTimers.containsKey(worldName);
  }

  public void endTimerForGame(String worldName) {
    checkIfNull("WorldName", worldName);
    if (!containsTimerForGame(worldName)) {
      throw new IllegalArgumentException(
          "You can not endTime before starting timer in class TimerService");
    }
    gameTimers.get(worldName).cancel();
    gameTimers.remove(worldName);
  }

  public int getRemainingTimeInMinutesForGame(String worldName) {
    checkIfNull("WorldName", worldName);
    if (!containsTimerForGame(worldName)) {
      throw new IllegalArgumentException(
          "You can not getRemainingTime before starting timer in class TimerService");
    }
    long timeInSeconds = gameTimers.get(worldName).GetRemainingTime();
    return (int) (timeInSeconds / 60);
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class TimerService");
    }
  }
}
