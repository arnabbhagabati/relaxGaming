package com.relax.main.controller;

import com.relax.main.beans.Game;
import com.relax.main.services.GameEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class RelaxController {

    @Autowired
    GameEngineService gameEngineService;

    //ToDo : Test parallel calls
    @RequestMapping(value = "/spinGame/{playerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity playGame(@PathVariable String playerId){
        Game game = null;
        try {
            game = gameEngineService.triggerGame(playerId);
        } catch (Exception e) {
            return new ResponseEntity("Your game encountered an error. Please contact support or try a new spin",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Game>(game, HttpStatus.OK);
    }

    @RequestMapping(value = "/gambleChoice/{playerId}/{gameId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity playGame(@PathVariable String playerId,@PathVariable String gameId){
        Double payout = 0.0;
        try {
            payout = gameEngineService.triggerGamble(gameId,playerId);
        } catch (Exception e) {
            return new ResponseEntity("Your game encountered an error. Please contact support or try a new spin",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Double>(payout, HttpStatus.OK);
    }
}
