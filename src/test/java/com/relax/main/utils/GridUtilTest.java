package com.relax.main.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GridUtilTest {

    @Value("#{${symbolProbabilityMap_Type1}}")
    private Map<String,Integer> symbolProbabilityMap;

    @Value("${gridSize_Type1}")
    private Integer gridSize;


    @Test
    void testGridGeneratedInCOrrectSize() {
        int gridSize = 5;
        GridUtil gridUtil = new GridUtil();

        List<List<String>> grid = gridUtil.generateGrid(gridSize, symbolProbabilityMap);

        assertEquals(gridSize, grid.size(), "Grid row count should match gridSize");
        for (List<String> row : grid) {
            assertEquals(gridSize, row.size(), "Each row must have gridSize elements");
        }
    }

    @Test
    void testGridGeneratedHasNonEMptyCells() {

        GridUtil gridUtil = new GridUtil();
        List<List<String>> grid = gridUtil.generateGrid(gridSize, symbolProbabilityMap);

        for (List<String> row : grid) {
            for (String symbol : row) {
                assertFalse(symbol.isEmpty(), "Grid Cell cannot be empty");
            }
        }
    }


}