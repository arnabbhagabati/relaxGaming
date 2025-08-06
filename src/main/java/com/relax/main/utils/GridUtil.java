package com.relax.main.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


public class GridUtil {

    private static final Logger LOG = LoggerFactory.getLogger(GridUtil.class);

    public static List<List<String>> generateGrid (int gridSize,Map<String,Integer> symbolProbabilityMap){

        List<List<String>> grid = new ArrayList<>();

        List<String> symbolList = new ArrayList<>();
        List<Integer> probabilitySumList = new ArrayList<>();
        Map<Integer,String> probNumMap = new HashMap<>();

        int sum =0;
        for(Map.Entry<String,Integer> e : symbolProbabilityMap.entrySet()){
           int probability = e.getValue();
           for(int i=1;i<=probability;i++){
               sum=sum+i;
               probNumMap.put(sum,e.getKey());
           }
        }
        LOG.info("Map contents: {}", probNumMap);

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
}
