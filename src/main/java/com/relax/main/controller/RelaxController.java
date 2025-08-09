package com.relax.main.controller;

import com.relax.main.beans.Game;
import com.relax.main.exceptions.GambleOnCompletedGameException;
import com.relax.main.services.AuthService;
import com.relax.main.services.GameEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/relax")
public class RelaxController {

    @Autowired
    GameEngineService gameEngineService;

    @Autowired
    AuthService authService;

    //ToDo : Test parallel calls
    @RequestMapping(value = "/spinGame", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity playGame(@RequestParam("betAmount") int betAmount){
        Game game = null;
        String userId = authService.authorizedPlayer();
        try {
            game = gameEngineService.triggerGame(userId,betAmount);
        } catch (Exception e) {
            return new ResponseEntity("Your game encountered an error. Please contact support or try a new spin",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Game>(game, HttpStatus.OK);
    }

    @RequestMapping(value = "/gambleChoice/{gameId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity triggerGamble(@PathVariable String gameId){
        Double payout = 0.0;
        String userId = authService.authorizedPlayer();
        try {
            payout = gameEngineService.triggerGamble(gameId,userId);
        } catch (GambleOnCompletedGameException e) {
            return new ResponseEntity("You attemted to gamble on a completed game. This is not possible",HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity("Your game encountered an error. Please contact support or try a new spin",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Double>(payout, HttpStatus.OK);
    }
}
