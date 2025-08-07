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
    MyProperties properties;

    @Autowired
    RelaxService relaxService;

    @GetMapping("/users")
    public Map<String, String> getEmployees(){
        Map<String, String> users = properties.getUsers();
        return users;
    }

    //ToDo : Test parallel calls
    @GetMapping("/game")
    public void playGame(){
        relaxService.triggerGame();
    }
}
