package com.WorldNavigator.Entities;


import com.WorldNavigator.Services.GameService;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    private long finishTime;
    private Timer timer;
    GameService gameService;
    String worldName;

    public GameTimer(String worldName) {
        timer = new Timer();
        finishTime=0;
        this.worldName = worldName;
    }

    public void setGameService(GameService gameService){
        this.gameService=gameService;
    }

    TimerTask timerTask =
            new TimerTask() {
                public void run() {
                    System.out.println("Game Over! You ran out of time.");
                    System.out.println("Better Luck Next Time");
                    gameService.endGame(worldName,"Game Over! You ran out of time.");
                }
            };

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public void schedule() {
        if (finishTime <= 0) {
            throw new IllegalArgumentException("finish time can not be less than or equal to zero in GameTimer.schedule.");
        }
        this.finishTime = System.currentTimeMillis() + finishTime * 1000;
        timer.schedule(timerTask, new Date(this.finishTime));
    }

    public long GetRemainingTime() {
        long timeLeft = Math.max(0, finishTime - System.currentTimeMillis());
        return timeLeft / 1000;
    }

    public long getFinishTime(){
        return finishTime;
    }

}

