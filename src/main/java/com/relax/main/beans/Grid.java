package com.relax.main.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private static final Logger LOG = LoggerFactory.getLogger(Grid.class);

    private List<List<String>> grid;

    public Grid(List<List<String>> grid) {
        this.grid = grid;
    }

    public List<List<String>> getGrid() {
        return grid;
    }

    public void setGrid(List<List<String>> grid) {
        this.grid = grid;
    }

    /*
     * prints the grid data on std out. O(n) complexity where n= total elements in grid
     * Not actually needed for the game engine
     * Todo : Use logging to print instead of syso
     */
    public void printGridData(){
        System.out.println();
        List<Integer> colWidths = new ArrayList<>();
        for(int j=0;j<grid.get(0).size();j++){
            int width = 0;
            for(int i=0;i<grid.size();i++){
                width = Math.max(grid.get(i).get(j).length(),width);
            }
            colWidths.add(width);
        }
        for(int i=0;i<grid.size();i++){
            System.out.print("|");
            for(int j=0;j<grid.size();j++){
                StringBuilder colVal = new StringBuilder().append(" ").append(grid.get(i).get(j)).append(" ");
                int remWidth = colWidths.get(j)-grid.get(i).get(j).length();
                for(int p=0;p<remWidth;p++){
                    colVal.append(" ");
                }
                colVal.append("|");
                System.out.print(colVal);
            }
            System.out.println();
        }
        System.out.println();
    }
}
