package com.relax.main.utils;

import com.relax.main.beans.Cluster;
import com.relax.main.beans.Cycle;
import com.relax.main.beans.Game;
import com.relax.main.beans.Grid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RelaxService {

    @Value("#{${symbolProbabilityMap_Type1}}")
    private Map<String,Integer> symbolProbabilityMap;

    @Value("${gridSize_Type1}")
    private Integer gridSize;

    @Autowired
    GridUtil gridUtil;

    private static final Logger LOG = LoggerFactory.getLogger(RelaxService.class);

    public Game triggerGame(){
        Game game = new Game();

        Grid grid = new Grid(gridUtil.generateGrid(gridSize,symbolProbabilityMap));
        game.setInitialGrid(grid.getGrid());
        grid.printGridData();

        List<Cycle> gameCycles = new ArrayList<>();
        while(true) {
            Cycle cycle = new Cycle();

            List<Cluster> clusters = gridUtil.findClusters(grid.getGrid());
            if(clusters.isEmpty()) break;
            cycle.setGridWithClusters(grid.getGrid());
            cycle.setClusters(clusters);
            grid.printGridData();
            for (Cluster cluster : clusters) System.out.println(cluster.toString());


            gridUtil.triggerAvalanche(grid.getGrid(), clusters);
            cycle.setGridWPostAvalanche(grid.getGrid());
            grid.printGridData();

            Grid refillGrid = gridUtil.generateRefillGrid(grid.getGrid(),symbolProbabilityMap);
            cycle.setRefillGrid(refillGrid.getGrid());
            refillGrid.printGridData();

            gridUtil.fillGrid(grid,refillGrid);
            cycle.setGridPostRefill(grid.getGrid());
            grid.printGridData();

            gameCycles.add(cycle);
        }

        game.setGameCycles(gameCycles);

        return game;
    }
}
