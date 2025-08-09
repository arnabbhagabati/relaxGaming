package com.relax.main.services;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    /*
      * This will typically use some auth library like Spring OAuth2
      * Along with Social login like Google Login/Facebook login
      * Not implemented for this exercise
     */
    public String authorizedPlayer(){
        return "player1";
    }
}
