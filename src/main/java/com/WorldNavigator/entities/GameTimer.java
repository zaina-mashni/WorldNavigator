package com.WorldNavigator.entities;

import com.WorldNavigator.services.GameService;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

  private long finishTime;
  private Timer timer;
  GameService gameService;
  String worldName;

  public GameTimer(String worldName) {
    checkIfNull("WorldName",worldName);
    timer = new Timer();
    this.worldName = worldName;
  }

  public void setGameService(GameService gameService) {
    checkIfNull("GameService",gameService);
    this.gameService = gameService;
  }

  TimerTask timerTask =
      new TimerTask() {
        public void run() {
          System.out.println("Game Over! You ran out of time.");
          System.out.println("Better Luck Next Time");
          gameService.endGame(worldName, "Game Over! You ran out of time.");
        }
      };

  public void schedule(long finishTimeInSeconds) {
    System.out.println("in schedule");
    if (finishTime <= 0) {
      throw new IllegalArgumentException(
          "finish time can not be less than or equal to zero in GameTimer.schedule.");
    }
    this.finishTime = System.currentTimeMillis() + finishTimeInSeconds * 1000;
    timer.schedule(timerTask, new Date(this.finishTime));
  }

  public long GetRemainingTime() {
    long timeLeft = Math.max(0, finishTime - System.currentTimeMillis());
    return timeLeft / 1000;
  }

  public void cancel() {
    timer.cancel();
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class GameTimer");
    }
  }
}
