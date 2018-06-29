package com.qainfotech.tap.training.snl.api;

/**
 *
 * @author Ramandeep
 */
public class MaxPlayersReachedExeption extends Exception{
    
    public MaxPlayersReachedExeption(Integer players){
        super("The board already has maximum allowed Player: " + players);
    }
}
