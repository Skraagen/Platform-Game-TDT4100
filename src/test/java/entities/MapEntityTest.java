package entities;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rendering.TileMap;

public class MapEntityTest {

	private TileMap map;
	private Player player;
	private int entityInitX = 32;
	private int entityInitY = 32;

	@BeforeEach
	public void setup() {
		map = new TileMap("testmap.txt", 30, 900, 900);
		try {
			map.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Using player as MapEntity implementation example
		player = new Player(map, entityInitX, entityInitY);
		player.setPosition();
		player.update();
	}
	
	@Test
	public void testCalculateCorners() {
		
		player.setPosition(player.getx(), entityInitY + 20); // Move entity into ground
		player.calculateCorners(player.getx(), player.gety());
		
		Assertions.assertEquals(true, player.bottomLeft || player.bottomRight, "Entity should trigger ground collision");
		
		player.update(); // Update position
		player.calculateCorners(player.getx(), player.gety());
		
		// The player is outside the solid blocks, therefore it doesn't detect any
		Assertions.assertEquals(false, player.topLeft, "Inaccurate top-left tile");
		Assertions.assertEquals(false, player.bottomLeft, "Inaccurate bottom-left tile");
		Assertions.assertEquals(false, player.bottomRight, "Inaccurate bottom-right tile");
		Assertions.assertEquals(false, player.topRight, "Inaccurate top-right tile");
	}
	
	@Test
	public void testCalculateCornersOnMoveRight() {
		player.calculateCorners(player.getx() + 10, player.gety());
		
		Assertions.assertEquals(false, player.topLeft || player.bottomLeft, "Inaccurate left tiles");
		Assertions.assertEquals(true, player.bottomRight || player.topRight, "Inaccurate right tiles");
	}
	
	@Test
	public void testCalculateCornersOnMoveLeft() {
		player.calculateCorners(player.getx() - 10, player.gety());
		
		//The player is outside the solid blocks, therefore it doesn't detect any
		Assertions.assertEquals(false, player.topLeft || player.bottomLeft, "Inaccurate left tiles");
		Assertions.assertEquals(false, player.bottomRight || player.topRight, "Inaccurate right tiles");
	}
	
	@Test
	public void testCalculateCornersOnJump() {
		player.calculateCorners(player.getx(), player.gety() + 10);
		
		//The player is outside the solid blocks, therefore it doesn't detect any
		Assertions.assertEquals(false, player.topLeft || player.topRight, "Inaccurate top tiles");
	}
	
	@Test
	public void testEntityCollision() {
		Worm worm = new Worm(map, player.getx(), player.gety());
		
		//Entities have same position so they should collide
		Assertions.assertEquals(true, player.intersects(worm));
		
		worm = new Worm(map, 0, player.gety() + 10);
		
		Assertions.assertEquals(false, player.intersects(worm));
	}
	
	@Test
	public void testPositionChange() {
		player.setPosition(12, 15);
		Assertions.assertEquals(12, player.getx());
		Assertions.assertEquals(15, player.gety());
		
		player.setPosition();
		Assertions.assertEquals(entityInitX, player.getx());
		Assertions.assertEquals(entityInitY, player.gety());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			player.setPosition(-100, -100);
		}, "Entities cannot be placed outside map");
	}
	
	@Test
	public void testEntityMoveInput() {
		player.setLeft(true);
		for (int i = 0; i < 10; i++) {
			player.update();
		}
		player.setLeft(false);
		
		Assertions.assertTrue(player.getx() < entityInitX);
		
		player.setRight(true);
		
		for (int i = 0; i < 1000; i++) {
			player.update();
		}
		
		int x = player.getx();
		
		for (int i = 0; i < 10000; i++) {
			player.update();
		}
		
		Assertions.assertEquals(x, player.getx(), "Entity should stop when hitting a solid block and calculating collision with CheckTileMapCollision");
	}
}
