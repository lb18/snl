package test;
import com.qainfotech.tap.training.snl.api.*;

import static org.testng.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class TestSnl
{	
	@Test(expectedExceptions = {PlayerExistsException.class})
	public void test_Player_Already_Exists_Condition() throws Exception
	{
		Board add_player = new Board();
		add_player.registerPlayer("Lokesh");
		add_player.registerPlayer("Lokesh");
	}
	
	@Test(expectedExceptions = {PlayerExistsException.class}, expectedExceptionsMessageRegExp = "Player 'Lokesh' already exists on board")
	public void test_Player_Already_Exists_Exception_Message() throws Exception
	{
		throw new PlayerExistsException("Lokesh");
	}
	
	@Test(expectedExceptions = {MaxPlayersReachedExeption.class})
	public void test_Max_Player_Condition() throws Exception
	{
		Board add_player = new Board();
		add_player.registerPlayer("player");
		add_player.registerPlayer("player2");
		add_player.registerPlayer("player3");
		add_player.registerPlayer("player4");
		add_player.registerPlayer("player5");
	}
	
	@Test(expectedExceptions = {MaxPlayersReachedExeption.class}, expectedExceptionsMessageRegExp = "The board already has maximum allowed Player: .*")
	public void test_Max_Player_Exception_Message() throws Exception
	{
		throw new MaxPlayersReachedExeption(4);
	}
	
	@Test(expectedExceptions = {GameInProgressException.class})
	public void starting_Postion_Check() throws Exception
	{
		Board add_player = new Board();
		add_player.registerPlayer("lokesh");
		JSONArray player_name = add_player.getData().getJSONArray("players");
		player_name.getJSONObject(0).put("position", 15);
		add_player.registerPlayer("sudhanshu");
		
	}
	
	@Test(expectedExceptions = {GameInProgressException.class}, expectedExceptionsMessageRegExp = "New player cannot join since the game has started")
	public void check_Game_In_Progress_Exception_Message() throws Exception
	{
		throw new GameInProgressException();
	}
	
	@Test(expectedExceptions = {InvalidTurnException.class})
	public void check_Invalid_Turn_Exception() throws Exception
	{
		Board add_player = new Board();
		//fromString(String name) method is used to create a UUID from the string 
		UUID uuid = UUID.fromString(add_player.registerPlayer("lokesh").getJSONObject(0).get("uuid").toString());
		UUID uuid2 = UUID.fromString(add_player.registerPlayer("sudhanshu").getJSONObject(1).get("uuid").toString());
		add_player.rollDice(uuid2);
	}
	
	@Test(expectedExceptions = {InvalidTurnException.class}, expectedExceptionsMessageRegExp = "Player .* does not have .*")
	public void check_Invalid_Turn_Exception_Message() throws Exception
	{
		Board add_player = new Board();
		UUID uuid = UUID.fromString(add_player.registerPlayer("lokesh").getJSONObject(0).get("uuid").toString());
		throw new InvalidTurnException(uuid);
	}
	
	/*@Test//(expectedExceptions = {NoUserWithSuchUUIDException.class})
	public void check_No_User_With_Such_UUID_Exception() throws Exception
	{
		Board add_player = new Board();
		UUID uuid = UUID.fromString(add_player.registerPlayer("lokesh").getJSONObject(0).get("uuid").toString());
		JSONArray player_name = add_player.deletePlayer(uuid);
		add_player.rollDice();
	}*/
	
	@Test(dependsOnMethods = {"check_Invalid_Turn_Exception_Message"})
	public void validate_Player_Data() throws Exception
	{
		Board add_player = new Board();
		String player_name = add_player.registerPlayer("Lokesh bisht").getJSONObject(0).getString("name");
		Assert.assertTrue(player_name.contains("Lokesh b"));
	}
	
	@Test(dependsOnMethods = {"validate_Player_Data"})
	public void valid_turn() throws Exception
	{
		Board add_player = new Board();
		UUID uuid = UUID.fromString((String) add_player.registerPlayer("lokesh").getJSONObject(0).get("uuid"));
		UUID uuid_of_first_player = (UUID) add_player.rollDice(uuid).get("playerUuid");
		assertEquals(uuid, uuid_of_first_player);
	}
	
}
