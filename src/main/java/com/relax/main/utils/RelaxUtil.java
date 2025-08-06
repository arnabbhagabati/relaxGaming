package com.relax.main.utils;

import com.relax.main.beans.Grid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RelaxUtil {

    @Value("#{${symbolProbabilityMap_Type1}}")
    private Map<String,Integer> symbolProbabilityMap;

    @Value("${gridSize_Type1}")
    private Integer gridSize;

    public void triggerGame(){
        Grid grid = new Grid(gridSize,symbolProbabilityMap);
        grid.printGridData();
    }
}
