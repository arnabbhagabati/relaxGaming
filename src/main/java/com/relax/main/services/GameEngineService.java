package com.relax.main.services;

import com.relax.main.beans.*;
import com.relax.main.exceptions.GambleOnCompletedGameException;
import com.relax.main.exceptions.NoDataFoundException;
import com.relax.main.utils.GameUtil;
import com.relax.main.utils.GridUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GameEngineService {

    @Value("#{${symbolProbabilityMap_Type1}}")
    private Map<String,Integer> symbolProbabilityMap;

    @Value("${gridSize_Type1}")
    private Integer gridSize;

    @Autowired
    GridUtil gridUtil;

    @Autowired
    GameRepository gameRepository;


    private static final Logger LOG = LoggerFactory.getLogger(GameEngineService.class);

    public Game triggerGame(String playerId, int betAmount) throws IOException {
        String gameId = GameUtil.generateGameId();
        Game game = new Game(gameId,playerId,betAmount);
        game.setStatus(GameStatus.IN_PROGRESS);

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
        game.setStatus(GameStatus.PENDING_GAMBLE);
        gameRepository.save(game);

        return game;
    }

    public Double triggerGamble(String gameId, String playerId) throws IOException, GambleOnCompletedGameException, NoDataFoundException {
        double payout = 0.0;

        Game savedGameData = gameRepository.getById(gameId);
        if(savedGameData==null){
            throw new NoDataFoundException();
        }
        if(savedGameData.getStatus().equals(GameStatus.COMPLETED)){
            throw new GambleOnCompletedGameException();
        }
        LOG.info("Gambling on game id {} for amount {}",savedGameData.getGameId(),savedGameData.getPayout());
        if(ThreadLocalRandom.current().nextBoolean()){
            payout =  savedGameData.getPayout()*2;
        }
        savedGameData.setStatus(GameStatus.COMPLETED);
        savedGameData.setPayout(payout);
        gameRepository.update(savedGameData);
        return payout;
    }
}
