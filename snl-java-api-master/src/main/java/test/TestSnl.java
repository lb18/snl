package test;
import com.qainfotech.tap.training.snl.api.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class TestSnl
{
	@Test(expectedExceptions = {PlayerExistsException.class}, expectedExceptionsMessageRegExp = "Player 'Lokesh' already exists on board")
	public void test_Player_Already_Exception_Message() throws Exception
	{
		throw new PlayerExistsException("Lokesh");
	}
	
	@Test(expectedExceptions = {PlayerExistsException.class})
	public void test_Player_Already_Exists_Condition() throws Exception
	{
		Board add_player = new Board();
		add_player.registerPlayer("Lokesh");
		add_player.registerPlayer("Lokesh");
	}
	
	@Test(expectedExceptions = {MaxPlayersReachedExeption.class}, expectedExceptionsMessageRegExp = "The board already has maximum allowed Player: .*")
	public void test_Max_Player_Exception_Message() throws Exception
	{
		throw new MaxPlayersReachedExeption(4);
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
	
	@Test(dependsOnMethods = {"test_Max_Player_Condition"})
	public void validate_Player_Data() throws Exception
	{
		Board add_player = new Board();
		String player_name = add_player.registerPlayer("Lokesh bisht").getJSONObject(0).getString("name");
		Assert.assertTrue(player_name.contains("Lokesh b"));
	}
	
}
