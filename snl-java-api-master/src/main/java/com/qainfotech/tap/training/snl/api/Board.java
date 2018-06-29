package com.qainfotech.tap.training.snl.api;

import java.util.UUID;
import java.util.Random;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author Ramandeep 
 */
public class Board {
    
    UUID uuid;
    JSONObject data;
    
    /**
     * construct a new board
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public Board()
            throws FileNotFoundException, UnsupportedEncodingException,
            IOException{
        uuid = UUID.randomUUID();
        BoardModel.init(uuid);
        data = BoardModel.data(uuid);
    }
    
    /**
     * access existing board object by uuid
     * @param uuid UUID of existing board
     * @throws IOException
     */
    public Board(UUID uuid) throws IOException {
        this.uuid = uuid;
        data = BoardModel.data(uuid);
    }
    
    /**
     * adds new player to board
     * Conditions: 
     *   - Max players 4
     *   - Players must have unique names on a board
     *   - All players are on initial position - 0th step
     * @param name - player name
     * @return JSONArray of registered players on the board with new player added
     * @throws PlayerExistsException exception thrown when entered name 
     *        parameter matches existing players
     * @throws GameInProgressException exception thrown when we try to register 
     *        on a board where players have already started movement
     * @throws MaxPlayersReachedExeption exception thrown when we try to register more 
     *        players than allowed limit for board
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public JSONArray registerPlayer(String name) 
            throws PlayerExistsException, GameInProgressException,
                FileNotFoundException, UnsupportedEncodingException,
                MaxPlayersReachedExeption, IOException {
        if(data.getJSONArray("players").length()==4){
            throw new MaxPlayersReachedExeption(4);
        }
        for(Object playerObject:data.getJSONArray("players")){
            JSONObject player = (JSONObject)playerObject;
            if(player.getString("name").equals(name)){
                throw new PlayerExistsException(name);
            }
            if(player.getInt("position")!=0){
                throw new GameInProgressException();
            }
        }
        JSONObject newPlayer = new JSONObject();
        newPlayer.put("name", name);
        newPlayer.put("uuid", UUID.randomUUID());
        newPlayer.put("position", 0);
        data.getJSONArray("players").put(newPlayer);
        BoardModel.save(uuid, data);
        return BoardModel.data(uuid).getJSONArray("players");
    }
    
    /**
     * deletes player from list if uuid matches
     * @param playerUuid UUID of player which has to be deleted
     * @return JSONArray of all existing players on the board
     * @throws NoUserWithSuchUUIDException raised when incorrect UUID is passed
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public JSONArray deletePlayer(UUID playerUuid)
            throws NoUserWithSuchUUIDException, FileNotFoundException,
                UnsupportedEncodingException{
        Boolean response = false;
        for(int i = 0; i < data.getJSONArray("players").length(); i++){
            JSONObject player = data.getJSONArray("players").getJSONObject(i);
            
            if(player.getString("uuid").equals(playerUuid.toString())){
                data.getJSONArray("players").remove(i);
                data.put("turn", 0);
                BoardModel.save(uuid, data);
                response = true;
            }
        }
        if(!response){
            throw new NoUserWithSuchUUIDException(playerUuid.toString());
        }
        return data.getJSONArray("players");
    }
    
    /**
     * Roll the dice of the turn player and make move on the board per the outcome
     * of the dice roll
     * @param playerUuid UUID of player who has the turn
     * @return JSONObject containing the outcome of dice, name and uuid of turn 
     * player and a message specifying the move that was made by the turn player
     * @throws InvalidTurnException raised when incorrect uuid is passed
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public JSONObject rollDice(UUID playerUuid) 
            throws InvalidTurnException, FileNotFoundException,
                UnsupportedEncodingException{
        JSONObject response = new JSONObject();
        Integer turn = data.getInt("turn");
        if(playerUuid.equals((UUID)data.getJSONArray("players").getJSONObject(turn).get("uuid"))){
            JSONObject player = data.getJSONArray("players").getJSONObject(turn);
            
            Integer dice = new Random().nextInt(6) + 1;
            Integer currentPosition = player.getInt("position");
            Integer newPosition = currentPosition + dice;
            String message = "";
            String playerName = player.getString("name");
            if(newPosition <= 100){
                JSONObject step = data.getJSONArray("steps").getJSONObject(newPosition);
                newPosition = step.getInt("target");
                if(step.getInt("type")==0){
                    message = "Player moved to " + newPosition;
                }else if(step.getInt("type")==1){
                    message = "Player was bit by a snake, moved back to " + newPosition;
                }else if(step.getInt("type")==2){
                    message = "Player climbed a ladder, moved to " + newPosition;
                }
                data.getJSONArray("players").getJSONObject(turn).put("position", newPosition);
            }else{
                message = "Incorrect roll of dice. Player did not move";
            }
            Integer newTurn = turn+1;
            if(newTurn >= data.getJSONArray("players").length()){
                newTurn = 0;
            }
            data.put("turn", newTurn);
            BoardModel.save(uuid, data);
            response.put("message", message);
            response.put("playerUuid", playerUuid);
            response.put("playerName", playerName);
            response.put("dice", dice);
            
        }else{
            throw new InvalidTurnException(playerUuid);
        }
        return response;
    }
    
    /**
     * pretty print the UUID of board
     * @return UUID of board as String
     */
    @Override
    public String toString(){
        return "UUID:" + uuid.toString() + "\n" + data.toString();
    }
    
    /**
     * return complete board data in JSON with turn, players list and steps
     * @return JSONObject board data containing steps, players and turn
     */
    public JSONObject getData(){
        return data;
    }
    
    /**
     * returns board uuid object
     * @return UUID of this board object
     */
    public UUID getUUID(){
        return uuid;
    }
}
