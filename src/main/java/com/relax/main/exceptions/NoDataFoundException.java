package com.relax.main.exceptions;

public class NoDataFoundException extends Exception{
    @Override
    public String getMessage(){
        return "Game data not found on database";
    }
}
