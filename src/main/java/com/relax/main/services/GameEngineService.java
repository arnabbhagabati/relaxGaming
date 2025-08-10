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
import java.math.BigDecimal;
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
        Game game = new Game(GameUtil.generateGameId(),playerId,betAmount);
        game.setStatus(GameStatus.IN_PROGRESS);
        Grid grid = new Grid(gridUtil.generateGrid(gridSize,symbolProbabilityMap));
        game.setInitialGrid(grid.getGrid());
        LOG.info("Starting a game with gameId {}, grid {} ",game.getGameId(),game.getInitialGrid());

        grid.printGridData();

        List<Cycle> gameCycles = new ArrayList<>();
        while(true) {
            Cycle cycle = new Cycle();

            List<Cluster> clusters = gridUtil.findClusters(grid.getGrid());
            if(clusters.isEmpty()) {
                LOG.info("No more clusters found");
                break;
            }
            cycle.setGridWithClusters(grid.getGrid());
            cycle.setClusters(clusters);
            grid.printGridData();
            LOG.info("Found clusters for gameId {}",clusters);
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
        LOG.info("game with  gameId {} completed",game.getGameId());

        return game;
    }

    //Todo : Validate the given gameId is for the same playerId
    public BigDecimal doubleOrNothing(String gameId, String playerId) throws IOException, GambleOnCompletedGameException, NoDataFoundException {
        BigDecimal payout = BigDecimal.valueOf(0);

        Game savedGameData = gameRepository.getById(gameId);
        if(savedGameData==null){
            throw new NoDataFoundException();
        }
        if(savedGameData.getStatus().equals(GameStatus.COMPLETED)){
            throw new GambleOnCompletedGameException();
        }

        LOG.info("doubleOrNothing on game id {} for amount {}",savedGameData.getGameId(),savedGameData.getPayout());
        if(ThreadLocalRandom.current().nextBoolean()){
            payout =  savedGameData.getPayout().multiply(BigDecimal.valueOf(2));
        }
        savedGameData.setStatus(GameStatus.COMPLETED);
        savedGameData.setPayout(payout);
        gameRepository.update(savedGameData);
        LOG.info("doubleOrNothing on game id {} complete. payout {}",savedGameData.getGameId(),payout);

        return payout;
    }
}
