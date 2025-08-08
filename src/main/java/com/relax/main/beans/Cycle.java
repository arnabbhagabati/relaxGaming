package com.relax.main.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cycle {

    private List<List<String>> gridWithClusters;
    private List<Cluster> clusters;
    private List<List<String>> gridWPostAvalanche;
    private List<List<String>> refillGrid;
    private List<List<String>> gridPostRefill;

    public List<List<String>> getGridWithClusters() {
        return gridWithClusters;
    }

    public void setGridWithClusters(List<List<String>> gridWithClusters) {
        this.gridWithClusters = copyGrid(gridWithClusters);
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public List<List<String>> getGridWPostAvalanche() {
        return gridWPostAvalanche;
    }

    public void setGridWPostAvalanche(List<List<String>> gridWPostAvalanche) {
        this.gridWPostAvalanche = copyGrid(gridWPostAvalanche);
    }

    public List<List<String>> getRefillGrid() {
        return refillGrid;
    }

    public void setRefillGrid(List<List<String>> refillGrid) {
        this.refillGrid = copyGrid(refillGrid);
    }

    public List<List<String>> getGridPostRefill() {
        return gridPostRefill;
    }

    public void setGridPostRefill(List<List<String>> gridPostRefill) {
        this.gridPostRefill = copyGrid(gridPostRefill);
    }

    private List<List<String>> copyGrid(List<List<String>> grid){
        List<List<String>> copiedGrid = new ArrayList<>();
        for(List<String> row : grid){
            copiedGrid.add(row.stream().collect(Collectors.toList()));
        }
        return copiedGrid;
    }
}
