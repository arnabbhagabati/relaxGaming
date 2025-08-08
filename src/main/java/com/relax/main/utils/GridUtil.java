package com.relax.main.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import com.relax.main.beans.Cluster;
import com.relax.main.beans.Grid;
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

        List<List<String>> table = new ArrayList<>();

        table.add(Arrays.asList("H3", "H2", "H1", "H2", "H4", "H4", "L6", "L5"));
        table.add(Arrays.asList("H2", "L7", "L8", "H3", "L7", "H1", "H1", "H2"));
        table.add(Arrays.asList("H2", "H2", "H2", "H4", "L8", "H2", "H4", "H2"));
        table.add(Arrays.asList("BL", "H4", "L5", "WR", "L5", "L5", "L6", "H2"));
        table.add(Arrays.asList("WR", "L5", "H2", "L8", "L5", "H2", "L5", "H2"));
        table.add(Arrays.asList("H2", "H2", "H2", "BL", "L5", "L5", "BL", "L5"));
        table.add(Arrays.asList("L5", "L6", "H2", "H2", "L5", "L5", "L5", "L7"));
        table.add(Arrays.asList("H2", "L5", "L5", "L8", "H2", "WR", "H2", "H2"));

        return grid;
    }

    //ToDo : Accept object of Grid instead of List<List>
    public List<Cluster> findClusters(List<List<String>> grid){
        int[][] traversed = new int[grid.size()][grid.size()];
        List<Cluster> clusters = new ArrayList<>();
        int clusterId = 0;
        for(int i=0;i<grid.size();i++){
            for(int j=0;j< grid.get(i).size();j++){
                if(traversed[i][j] == 0 && !grid.get(i).get(j).equals("WR")){
                    Set<GridCell> cluster = bfs(grid,traversed,i,j);
                    if(cluster != null){
                        clusters.add(new Cluster(cluster,clusterId++,grid.get(i).get(j)));
                    }
                }
            }
        }
        calculatePayout(grid,clusters);
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

    public Grid generateRefillGrid(List<List<String>> grid, Map<String,Integer> symbolProbabilityMap) {
        Map<String,Integer> symbolMap = new HashMap<>();
        symbolMap.putAll(symbolProbabilityMap);
        symbolMap.remove("BL");  // Since BLOCKER is not used for refill post avalanche
        Map<Integer,String> probNumMap = new HashMap<>();

        // Create a pre-initialzed grid.
        // ToDo - maybe there is a better way to do this?
        List<List<String>> refill = new ArrayList<>();
        for(int i=0;i<grid.size();i++){
            List<String> list = new ArrayList<>();
            for(int j=0;j<grid.get(0).size();j++){
                list.add("");
            }
            refill.add(list);
        }

        int sum =0;
        for(Map.Entry<String,Integer> e : symbolMap.entrySet()){
            int probability = e.getValue();
            for(int i=1;i<=probability;i++){
                sum++;
                probNumMap.put(sum,e.getKey());
            }
        }

        for(int j=0;j<grid.get(0).size();j++){
            for(int i=0;i<grid.size();i++){
                if(grid.get(i).get(j).isEmpty()){
                    int randomNumber = ThreadLocalRandom.current().nextInt(1, sum+1);
                    String symbol = probNumMap.get(randomNumber);
                    refill.get(i).set(j,symbol);
                }else{
                    break;
                }
            }
        }

        Grid refillGridObj = new Grid(refill);
        return refillGridObj;
    }

    public void fillGrid(Grid grid,Grid refillGrid) {
        List<List<String>> orgGrid = grid.getGrid();
        for(int i=0;i<orgGrid.size();i++){
            for(int j=0;j<orgGrid.get(0).size();j++){
                if(orgGrid.get(i).get(j).isEmpty()){
                    orgGrid.get(i).set(j,refillGrid.getGrid().get(i).get(j));
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



    private Set<GridCell> bfs(List<List<String>> grid, int[][] traversed, int i, int j) {
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


        // Mark all wild card cells as not traversed, as WR are eligible for other clusters as well
        for(GridCell gc : cluster){
            if(gc.getData().equals("WR")) traversed[gc.getX()][gc.getY()] =0;
        }

        // ToDo  : Move this hardcoded num 5 to properties
        if(cluster.size()>=5){
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

    private void calculatePayout(List<List<String>> grid, List<Cluster> clusters) {

        //ToDo Move this to properties file
        Map<String, TreeMap<Integer, Integer>> payoutMap = new HashMap<>();

        payoutMap.put("H1", new TreeMap<>(Map.of(5, 5, 9, 6, 13, 7, 17, 8, 21, 10)));
        payoutMap.put("H2", new TreeMap<>(Map.of(5, 4, 9, 5, 13, 6, 17, 7, 21, 9)));
        payoutMap.put("H3", new TreeMap<>(Map.of(5, 4, 9, 5, 13, 6, 17, 7, 21, 9)));
        payoutMap.put("H4", new TreeMap<>(Map.of(5, 3, 9, 4, 13, 5, 17, 6, 21, 7)));

        payoutMap.put("L5", new TreeMap<>(Map.of(5, 1, 9, 2, 13, 3, 17, 4, 21, 5)));
        payoutMap.put("L6", new TreeMap<>(Map.of(5, 1, 9, 2, 13, 3, 17, 4, 21, 5)));
        payoutMap.put("L7", new TreeMap<>(Map.of(5, 1, 9, 2, 13, 3, 17, 4, 21, 5)));
        payoutMap.put("L8", new TreeMap<>(Map.of(5, 1, 9, 2, 13, 3, 17, 4, 21, 5)));

        for(Cluster cluster : clusters){
            String sym = cluster.getSymbol();
            double payOut =0;
            int count = 0;
            for(GridCell gc : cluster.getCells()){
                if(!gc.getData().equals("WR")){
                    count++;
                }
            }
            for(Map.Entry<Integer,Integer> entry : payoutMap.get(sym).entrySet()){
                if(count>=entry.getKey()){
                    payOut = entry.getValue();
                }else{
                    break;
                }
            }
            cluster.setPayout(payOut);
        }
    }

}
