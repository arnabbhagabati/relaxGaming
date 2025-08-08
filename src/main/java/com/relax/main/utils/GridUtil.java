package com.relax.main.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import com.relax.main.beans.Cluster;
import com.relax.main.beans.GridCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GridUtil {

    //ToDo : Remove hardcoded strings like "WR", "BL"

    private static final Logger LOG = LoggerFactory.getLogger(GridUtil.class);
    private static final String DEMARCATOR ="-";

    public List<List<String>> generateGrid (int gridSize,Map<String,Integer> symbolProbabilityMap){

        List<List<String>> grid = new ArrayList<>();
        Map<Integer,String> probNumMap = new HashMap<>();

        int sum =0;
        for(Map.Entry<String,Integer> e : symbolProbabilityMap.entrySet()){
           int probability = e.getValue();
           for(int i=1;i<=probability;i++){
               sum++;
               probNumMap.put(sum,e.getKey());
           }
        }
        LOG.info("probNumMap contents: {}", probNumMap);

        for(int i=0;i<gridSize;i++){
            List<String> row = new ArrayList<>();
            for(int j=0;j<gridSize;j++){
                int randomNumber = ThreadLocalRandom.current().nextInt(1, sum+1);
                String symbol = probNumMap.get(randomNumber);
                row.add(symbol);
            }
            grid.add(row);
        }

        return grid;
    }


    public List<Cluster> findClusters(List<List<String>> grid){
        int[][] traversed = new int[grid.size()][grid.size()];
        List<Cluster> clusters = new ArrayList<>();
        int clusterId = 0;
        for(int i=0;i<grid.size();i++){
            for(int j=0;j< grid.get(i).size();j++){
                if(traversed[i][j] == 0 && !grid.get(i).get(j).equals("WR")){
                    Set<GridCell> cluster = dfs(grid,traversed,i,j);
                    if(cluster != null){
                        clusters.add(new Cluster(cluster,clusterId++));
                    }
                }
            }
        }
        updateGridWithClusters(grid,clusters);

        return clusters;
    }


    public void triggerAvalanche(List<List<String>> grid,List<Cluster> clusters){
        markBlockersForDestruction(grid,clusters);
        for(int j=0;j<grid.get(0).size();j++){
            int fallCount = 0;
            for(int i=grid.size()-1;i>=0;i--){
                String sym = grid.get(i).get(j);
                if(sym.contains(DEMARCATOR)){
                    fallCount++;
                    grid.get(i).set(j,"");  // Destroy the cell
                }else{
                    if(fallCount>0){
                        //Move the cell down
                        grid.get(i+fallCount).set(j,sym);
                        grid.get(i).set(j,"");
                    }
                }
            }
        }
    }


    private void markBlockersForDestruction(List<List<String>> grid,List<Cluster> clusters){
        for(Cluster cluster : clusters) {
            for (GridCell gc : cluster.getCells()) {
                int x = gc.getX();
                int y=  gc.getY();

                if(isValid(grid,x+1,y) && grid.get(x+1).get(y).equals("BL")){
                    grid.get(x+1).set(y, "BL" + DEMARCATOR  + cluster.getId());
                }

                if(isValid(grid,x-1,y) && grid.get(x-1).get(y).equals("BL")){
                    grid.get(x-1).set(y, "BL" + DEMARCATOR  + cluster.getId());
                }

                if(isValid(grid,x,y+1) && grid.get(x).get(y+1).equals("BL")){
                    grid.get(x).set(y+1, "BL" + DEMARCATOR  + cluster.getId());
                }

                if(isValid(grid,x,y-1) && grid.get(x).get(y-1).equals("BL")){
                    grid.get(x).set(y-1, "BL" + DEMARCATOR  + cluster.getId());
                }
            }
        }
    }



    private Set<GridCell> dfs(List<List<String>> grid, int[][] traversed, int i, int j) {
        String sym = grid.get(i).get(j);
        Queue<GridCell> q = new LinkedList<>();
        q.add(new GridCell(i,j,sym));
        Set<GridCell> cluster = new HashSet<>();

        while(!q.isEmpty()){
            GridCell gi = q.poll();
            int x = gi.getX();
            int y= gi.getY();
            if(traversed[x][y] ==1){
                continue;
            }
            cluster.add(gi);
            traversed[x][y] = 1;

            if(isValid(grid,x+1,y) &&
                        (grid.get(x+1).get(y).equals(sym) || grid.get(x+1).get(y).equals("WR"))){
                q.add(new GridCell(x+1,y,grid.get(x+1).get(y)));
            }

            if(isValid(grid,x-1,y) &&
                        (grid.get(x-1).get(y).equals(sym) || grid.get(x-1).get(y).equals("WR"))){
                q.add(new GridCell(x-1,y,grid.get(x-1).get(y)));
            }
            if(isValid(grid,x,y+1) &&
                    (grid.get(x).get(y+1).equals(sym) || grid.get(x).get(y+1).equals("WR"))){
                q.add(new GridCell(x,y+1,grid.get(x).get(y+1)));
            }
            if(isValid(grid,x,y-1) &&
                    (grid.get(x).get(y-1).equals(sym) || grid.get(x).get(y-1).equals("WR"))){
                q.add(new GridCell(x,y-1,grid.get(x).get(y-1)));
            }
        }

        // ToDo  : Move this hardcoded num 5 to properties
        if(cluster.size()>=5){
            // Mark all wild cards as not traversed, as are eligible for other clusters as well
            for(GridCell gc : cluster){
                if(gc.getData().equals("WR")) traversed[gc.getX()][gc.getY()] =0;
            }
            return cluster;
        }

        return null;

    }

    private boolean isValid(List<List<String>> grid, int x, int y){
        int height = grid.size();
        int width = grid.get(0).size();

        if((x>=0 && x<height) && (y>=0 && y<width)){
            return true;
        }
        return false;
    }

    private void updateGridWithClusters(List<List<String>> grid,List<Cluster> clusters) {

        for(Cluster cluster : clusters) {
            for (GridCell gc : cluster.getCells()) {
                int x = gc.getX();
                int y=  gc.getY();
                String sym = grid.get(x).get(y);
                grid.get(x).set(y, sym + DEMARCATOR + cluster.getId());
            }
        }
    }

}
