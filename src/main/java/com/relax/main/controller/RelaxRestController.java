package com.relax.main.controller;

import com.relax.main.MyProperties;
import com.relax.main.utils.RelaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;


@RestController
public class RelaxRestController {

    @Autowired
    RelaxService relaxService;

    //ToDo : Test parallel calls
    @GetMapping("/game")
    public void playGame(){
        relaxService.triggerGame();
    }
}
