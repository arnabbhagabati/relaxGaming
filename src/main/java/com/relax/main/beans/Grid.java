package com.relax.main.beans;

import com.relax.main.utils.GridUtil;

import java.util.List;
import java.util.Map;


public class Grid {

    private int gridSize;
    private Map<String,Integer> symProbabMap;
    private List<List<String>> grid;
    private double payout;

    public Grid(int gridSize, Map<String, Integer> symProbabMap) {
        this.gridSize = gridSize;
        this.symProbabMap = symProbabMap;
        this.grid = GridUtil.generateGrid(gridSize,symProbabMap);
    }

    public double getPayout() {
        return payout;
    }

    public void setPayout(double payout) {
        this.payout = payout;
    }

    public List<List<String>> getGrid() {
        return grid;
    }

    public void setGrid(List<List<String>> grid) {
        this.grid = grid;
    }

    public void printGridData(){
        for(List<String> row : grid){
            System.out.print("|");
            for(String symbol : row){
                System.out.print(" "+symbol+" |");
            }
            System.out.println();
        }
    }
}
