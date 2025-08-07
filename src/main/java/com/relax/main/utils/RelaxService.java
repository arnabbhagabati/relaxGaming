package com.relax.main.utils;

import com.relax.main.beans.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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

    public void triggerGame(){
        Grid grid = new Grid(gridUtil.generateGrid(gridSize,symbolProbabilityMap));
        grid.printGridData();
        gridUtil.findClusters(grid.getGrid());
        grid.printGridData();
    }
}
