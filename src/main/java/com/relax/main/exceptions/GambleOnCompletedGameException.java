package com.relax.main.exceptions;

public class GambleOnCompletedGameException extends  Exception{

    @Override
    public String getMessage(){
        return "This game has completed. Gambling on this game is not allowed";
    }
}
