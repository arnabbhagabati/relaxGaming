package com.relax.main.beans;

public class SavedGameData {

    private String gameId ;
    private String playerId;
    private String winAmount ;
    private String status;

    public SavedGameData(String gameId, String playerId, String winAmount, String status) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.winAmount = winAmount;
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

    public String getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(String winAmount) {
        this.winAmount = winAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
