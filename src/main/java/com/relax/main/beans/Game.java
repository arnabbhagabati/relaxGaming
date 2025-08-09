package com.relax.main.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private String gameId;
    private String playerId;
    private List<List<String>> initialGrid;
    private List<Cycle> gameCycles;
    private double payout;

    public Game(String gameId, String playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
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
        for(Cycle cycle :gameCycles){
            this.payout = this.payout+cycle.getPayout();
        }
    }

    public double getPayout() {
        return payout;
    }

    public void setPayout(double payout) {
        this.payout = payout;
    }
}
