package com.relax.main.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private List<List<String>> initialGrid;
    private List<Cycle> gameCycles;

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
    }
}
