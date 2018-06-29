package com.qainfotech.tap.training.snl.api;

/**
 *
 * @author Ramandeep
 */
public class PlayerExistsException extends Exception{
    
    public PlayerExistsException(String name){
        super("Player '"+name+"' already exists on board");
    }
}
