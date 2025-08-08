package com.relax.main.controller;

import com.relax.main.beans.Game;
import com.relax.main.utils.RelaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class RelaxController {

    @Autowired
    RelaxService relaxService;

    //ToDo : Test parallel calls
    @RequestMapping(value = "/game", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Game playGame(){
        Game game = relaxService.triggerGame();
        return game;
    }
}
