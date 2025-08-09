package com.relax.main.beans;

import com.relax.main.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private int betAmount;
    private String gameId;
    private String playerId;
    private List<List<String>> initialGrid;
    private List<Cycle> gameCycles;
    private double payout;
    private GameStatus status;

    public Game(String gameId, String playerId, int betAmount) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.betAmount = betAmount;
    }

    public Game(String gameId, String playerId, String winAmount, GameStatus status) {
        this.gameId=gameId;
        this.playerId = playerId;
        this.payout=Double.parseDouble(winAmount);
        this.status = status;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public List<List<String>> getInitialGrid() {
        return initialGrid;
    }

    public void setInitialGrid(List<List<String>> grid) {
        /*
         * Keep a copy of the original grid
         * Actual grid will be modified as game progresses
         */
        this.initialGrid = new ArrayList<>();
        for(List<String> row : grid){
            initialGrid.add(row.stream().collect(Collectors.toList()));
        }
    }

    public List<Cycle> getGameCycles() {
        return gameCycles;
    }

    public void setGameCycles(List<Cycle> gameCycles) {
        this.gameCycles = gameCycles;
        double payoutonTen = 0.00;
        for(Cycle cycle :gameCycles) payoutonTen = payoutonTen+cycle.getPayout();
        this.payout = GameUtil.calculateAcrualPayout(betAmount,payoutonTen);
    }

    public double getPayout() {
        return payout;
    }

    public void setPayout(double payout) {
        this.payout = payout;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }
}
