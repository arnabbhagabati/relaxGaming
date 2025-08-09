package com.relax.main.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GridUtilTest {


    @Test
    void testGridGeneratedInCOrrectSize() {
        int gridSize = 5;
        Map<String, Integer> symbolProbabilityMap = new HashMap<>();
        symbolProbabilityMap.put("H1", 2);
        symbolProbabilityMap.put("H2", 3);
        symbolProbabilityMap.put("L1", 1);

        GridUtil gridUtil = new GridUtil();

        List<List<String>> grid = gridUtil.generateGrid(gridSize, symbolProbabilityMap);

        assertEquals(gridSize, grid.size(), "Grid row count should match gridSize");
        for (List<String> row : grid) {
            assertEquals(gridSize, row.size(), "Each row must have gridSize elements");
        }
    }

    @Test
    void testGridGeneratedHasNonEMptyCells() {
        int gridSize = 5;
        Map<String, Integer> symbolProbabilityMap = new HashMap<>();
        symbolProbabilityMap.put("H1", 2);
        symbolProbabilityMap.put("H2", 3);
        symbolProbabilityMap.put("L1", 1);

        GridUtil gridUtil = new GridUtil();
        List<List<String>> grid = gridUtil.generateGrid(gridSize, symbolProbabilityMap);

        for (List<String> row : grid) {
            for (String symbol : row) {
                assertFalse(symbol.isEmpty(), "Grid Cell cannot be empty");
            }
        }
    }


}