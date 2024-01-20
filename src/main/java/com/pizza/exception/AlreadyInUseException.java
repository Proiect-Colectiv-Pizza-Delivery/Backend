package com.pizza.exception;

public class AlreadyInUseException extends Exception{
    public AlreadyInUseException(String msg){
        super(msg);
    }
}