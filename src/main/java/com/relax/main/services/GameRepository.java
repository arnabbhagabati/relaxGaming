package com.relax.main.services;

import com.relax.main.beans.Game;
import com.relax.main.beans.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.*;


/*
``This is a dummy service to simulate persistent storage.
  For this exercise, this service just writes and reads from a csv file
  In a real application this would save information to a real database or in-memory store
 */

@Service
public class GameRepository {

    private static final Logger LOG = LoggerFactory.getLogger(GameRepository.class);
    private static final String TABLE_NAME ="GameWinData";

    public void save(Game game) throws IOException {

        Path path = Paths.get("src/main/resources/"+TABLE_NAME+".csv");

        // Check if file exists; if not, create it with appropriate headers
        if (!Files.exists(path)) {
            LOG.warn("CSV file does not exist. Creating new file with headers.");
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write("GameId,PlayerId,Winning,Status");
                writer.newLine();
            } catch (IOException e) {
                LOG.error("Error encountered saving data for gameId {} ",game.getGameId(),e);
                throw e;
            }
        }

        // Save actual data
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            List<String> row = List.of(game.getGameId(),game.getPlayerId(),String.valueOf(game.getPayout()),game.getStatus().toString());
            writer.write(String.join(",", row));
            writer.newLine();
            LOG.info("Data saved to " + path.toString());
        } catch (Exception e) {
            LOG.error("Error encountered saving data for gameId {} ",game.getGameId(),e);
            throw e;
        }
    }


    public Game getById(String gameIdIn){
        Path path = Paths.get("src/main/resources/"+TABLE_NAME+".csv");
        Game savedGameData = null;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] columns = line.split(",");
                String gameId = columns[0].trim();
                String playerId = columns[1].trim();
                String winAmount = columns[2].trim();
                String status = columns[3].trim();

                if(gameId.equals(gameIdIn)){
                    savedGameData = new Game(gameId,playerId,winAmount, GameStatus.valueOf(status));
                    break;
                }

            }
        } catch (Exception e) {
            LOG.error("Error encountered retrieving data for playerId {}, gameId ",gameIdIn,e);
        }

        return savedGameData;
    }


    public void update(Game game) throws IOException {

        try {
            Path path = Paths.get("src/main/resources/" + TABLE_NAME + ".csv");
            List<String> lines = Files.readAllLines(path);
            List<String[]> table = new ArrayList<>();
            int targetRow = -1;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] currRow = line.split(",", -1);
                table.add(line.split(",", -1));
                if (currRow[0].equals(game.getGameId())) targetRow = i;
            }

            table.get(targetRow)[2] = String.valueOf(game.getPayout());
            table.get(targetRow)[3] = game.getStatus().toString();

            List<String> updatedLines = new ArrayList<>();
            for (String[] row : table) {
                updatedLines.add(String.join(",", row));
            }

            Files.write(Paths.get("src/main/resources/" + TABLE_NAME + ".csv"), updatedLines);
        }catch(Exception e){
            LOG.error("Error encountered updating data for gameId {} ",game.getGameId(),e);
            throw e;
        }

    }
}
