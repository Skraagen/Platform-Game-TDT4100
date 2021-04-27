package rendering;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.Coin;
import entities.MapEntity;
import entities.Player;
import main.Config;
import main.Config.BlockType;

public class TileMapTest {

	private TileMap map;
	private String testMapPath = "testmap.txt";

	@BeforeEach
	public void setup() {
		map = new TileMap(testMapPath, 30, 900, 900);
		try {
			map.build();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("The mapfile needs to be have correct format before running TileMapTest.");
		}
	}
	
	public String getTestMapAsString() {
		String out = "";
		
		Scanner s;
		try {
			s = new Scanner(new FileReader(testMapPath));
			while(s.hasNext()){
		    	out += s.nextLine() + "\n";
		    }
		    
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
	    return out;
	}
	
	@Test
	public void testBuildMap() {
		
		int entityCount = 0;
		Iterator<MapEntity> entityIterator = map.entityIterator();
		
		while (entityIterator.hasNext()) {
			entityIterator.next();
			entityCount++;
		}
		
		int expectedEntityCount = 0;
		
		Pattern pattern = Pattern.compile("X|C|O|D");
		Matcher matcher = pattern.matcher(getTestMapAsString());
		
		while (matcher.find()) {
			expectedEntityCount++;
		}
		
		Assertions.assertEquals(expectedEntityCount, entityCount, "Expected entity count doesn't match entities loaded");
		Assertions.assertEquals(getTestMapAsString().split("\\r?\\n")[1], Integer.toString(map.getHeight() / map.getTileSize()), "Loaded map height doesn't match actual height");
		Assertions.assertEquals(getTestMapAsString().split("\\r?\\n")[0], Integer.toString(map.getWidth() / map.getTileSize()), "Loaded map width doesn't match actual width");
		
		map = new TileMap("nonexistantmap.txt", 30, 900, 900);
		
		Assertions.assertThrows(IOException.class, () -> {
			map.build();
		}, "Map build didn't throw IOException on missing mapfile");
	}
	
	@Test
	public void testTileTypes() {
		
		char[] mapTokens = getTestMapAsString().replace("\n", "").toCharArray();
		
		for (int i = 2; i < mapTokens.length; i++) {
			int row = (int) ((i - 2) / (map.getWidth() / map.getTileSize()));
			int col = (i - 2) % (map.getHeight() / map.getTileSize() + 1);
			
			Assertions.assertEquals(Config.TileType.getValue(mapTokens[i]).getType(), map.getType(row, col),
					"The map tiles doesn't match with actual tiles from file. Error happened at row: " + row + " and col: " + col);			
		}
		
		// Tiles outside map (Outside map on x-coordinate should be solid and clear on y-coordinate)
		Assertions.assertEquals(BlockType.CLEAR, map.getType(-1000, 1), "Inaccurate type");
		Assertions.assertEquals(BlockType.CLEAR, map.getType(1000, 1), "Inaccurate type");
		Assertions.assertEquals(BlockType.SOLID, map.getType(-1000, -1000), "Inaccurate type");
		Assertions.assertEquals(BlockType.SOLID, map.getType(1, -100), "Inaccurate type");
		Assertions.assertEquals(BlockType.SOLID, map.getType(1, 100), "Inaccurate type");
	}
	
	@Test
	public void testMapPosition() {
		
		Assertions.assertEquals(0, map.getx());
		
		map.setPosition(-20, 0);
		
		Assertions.assertEquals(0, map.getx());
		
		map.setPosition(10000, 0);
		
		Assertions.assertEquals(0, map.getx());
		
		for (int i = 0; i < 2000; i++) {
			map.setPosition(0, 400);
		}
		
		int tempY = map.gety();
		
		for (int i = 0; i < 2000; i++) {
			map.setPosition(0, -300);
		}
		
		Assertions.assertEquals(tempY, map.gety(), "Incorrect Y-coordinate. The screen should always animate towards a target Y");
	}
	
	@Test
	public void testPrimaryEntitiesLoaded() {
		int playerCount = 0;
		int coinCount = 0;
		
		Iterator<MapEntity> entityIterator = map.entityIterator();
		
		while (entityIterator.hasNext()) {
			MapEntity entity = entityIterator.next();
			
			if (entity instanceof Player) {
				playerCount++;
			} else if (entity instanceof Coin) {
				coinCount++;
			}
		}
		
		Assertions.assertEquals(1, playerCount, "The game needs to have exactly 1 player");
		Assertions.assertTrue(coinCount > 0, "The game needs to have at least 1 coin");
	}
}