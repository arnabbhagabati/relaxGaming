package com.relax.main.services;

import com.relax.main.beans.SavedGameData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.io.*;
import java.nio.file.*;
import java.util.*;


/*
``This is a dummy service to simulate persistent storage.
  For this exercise, this service just writes and reads from a csv file
  In real application this would save information to a real database or in memory store
 */

@Service
public class DatabaseService {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseService.class);
    private static final String TABLE_NAME ="GameWinData";

    public void saveGame(String gameId,String playerId,double wins,String status) throws IOException {

        Path path = Paths.get("src/main/resources/"+TABLE_NAME+".csv");

        // Check if file exists; if not, create it with appropriate headers
        if (!Files.exists(path)) {
            LOG.warn("CSV file does not exist. Creating new file with headers.");
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write("GameId,PlayerId,Winning,Status");
                writer.newLine();
            } catch (IOException e) {
                LOG.error("Error encountered saving data for playerId {}, gameId {} ",playerId,gameId,e);
                throw e;
            }
        }

        // Save actual data
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            List<String> row = List.of(gameId,playerId,String.valueOf(wins),status);
            writer.write(String.join(",", row));
            writer.newLine();
            LOG.warn("Data saved to " + path.toString());
        } catch (IOException e) {
            LOG.error("Error encountered saving data for playerId {}, gameId {} ",playerId,gameId,e);
            throw e;
        }
    }


    public SavedGameData readFromCsv(String gameIdIn){
        Path path = Paths.get("src/main/resources/"+TABLE_NAME+".csv");
        SavedGameData savedGameData = null;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] columns = line.split(",");
                String gameId = columns[0].trim();
                String playerId = columns[1].trim();
                String winAmount = columns[2].trim();
                String status = columns[3].trim();

                if(gameId.equals(gameIdIn)){
                    savedGameData = new SavedGameData(gameId,playerId,winAmount,status);
                    break;
                }

            }
        } catch (IOException e) {
            LOG.error("Error encountered retrieving data for playerId {}, gameId ",gameIdIn,e);
        }

        return savedGameData;
    }


}
