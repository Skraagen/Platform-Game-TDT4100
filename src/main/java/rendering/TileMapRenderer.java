package rendering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import entities.Coin;
import entities.Drop;
import entities.MapEntity;
import entities.Player;
import entities.Worm;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import listeners.LevelListener;
import main.Config;

public class TileMapRenderer extends AnimationTimer {

	private Canvas canvas;
	private TileMap map;
	private int totalCoins;
	private Player player;
	private Collection<LevelListener> listeners;
	private GraphicsContext g;
	private Iterator<MapEntity> entityIterator;
	
	public TileMapRenderer(Canvas canvas) throws IOException {
		this.canvas = canvas;
		this.listeners = new ArrayList<>();
		this.g = canvas.getGraphicsContext2D();

		initMap();
		initTimer(Config.FPS);
		startTimer();
	}
	
	public void initMap() throws IOException {
		map = new TileMap(Config.TILE_MAP_PATH, 30, (int) canvas.getWidth(), (int) canvas.getHeight());
		map.build();
		entityIterator = map.entityIterator();
		
		while (entityIterator.hasNext()) {
			MapEntity entity = entityIterator.next();
			
			entity.loadImage();
			
			if (entity instanceof Coin) {
				totalCoins++;
			}
			
			if (entity instanceof Player) {
				player = (Player) entity;
			}
		};
		
		map.loadMapImage();
	}
	
	// Clear screen each frame
	public void prepare() {
		g.setFill(new Color(0.4, 0.64, 0.59, 1));
	    g.fillRect(0,0, canvas.getWidth(),canvas.getHeight());
	}
	
	// Render map and update entity/map states
	@Override
	public void loop() {
		prepare();
		
		entityIterator = map.entityIterator();
		
    	while (entityIterator.hasNext()) {
    		MapEntity entity = entityIterator.next();
    		entity.draw(g);
    		entity.update();
    	    
    		// Kills player on interaction with dangerous entities
    		if (entity instanceof Drop || entity instanceof Worm) {
	    	    if (player.intersects(entity)) {
	    	    	player.setDead(true);
	    	    }
    		}
    		
    		// Rewards player on interaction with coins
    		if (entity instanceof Coin) {
	    	    if (player.intersects(entity)) {
	    	    	firePlayerCollect();
	    	    	entityIterator.remove();
	    	    }
    		}
    		
    		if (entity.isCollidingWithDangerBlock()) {
    			entity.setPosition();
    		}
    	};
    	
    	if (player.isCollidingWithDangerBlock()) player.setDead(true); // Check if player is colliding with dangerblock
		if (player.isDead()) { firePlayerDead(); }; // Check if player is dead
		if (player.getHealth() <= 0) { firePlayerLose(); } // Check if player is dead
		if (player.getCoins() == totalCoins) { firePlayerWin(); } // Check if player has won
    	
		// Set map position such that player is in the middle if possible
    	map.setPosition(canvas.getWidth() / 2 - player.getx(), canvas.getHeight() / 2 - player.gety());
    	map.draw(g);
	}
	
	public int getTotalCoins() {
		return totalCoins;
	}
	
	public int getPlayerMaxHealth() {
		return player.getMaxHealth();
	}
	
	private void firePlayerDead() {
		player.setPosition();
		player.setHealth(player.getHealth() - 1);
		player.setDead(false);
		
		for(LevelListener listener : getListeners()) {
			listener.playerDead(player.getHealth() - 1);
		}
	}

	private void firePlayerCollect() {
		player.addCoin();
		
		for(LevelListener listener : getListeners()) {
			listener.playerCollect(player.getCoins());
		}
	}

	private void firePlayerLose() {
		for(LevelListener listener : getListeners()) {
			listener.playerLose();
		}
	}

	private void firePlayerWin() {
		for(LevelListener listener : getListeners()) {
			listener.playerWin();
		}
	}
	
	public void addLevelRendererListener(LevelListener listener) {
		listeners.add(listener);
	}
	
	public void removeLevelRendererListener(LevelListener listener) {
		listeners.remove(listener);
	}
	
	public Collection<LevelListener> getListeners() {
		return listeners;
	}
	
	public void movePlayer(String movement) {
    	switch (movement) {
	        case "left": player.setLeft(true); break;
	        case "jump": player.setJumping(true); break;
	        case "right": player.setRight(true); break;
			default: throw new IllegalArgumentException("The player can't do this movement!");
    	}
	}
	
	public void stopMovePlayer(String movement) {
    	switch (movement) {
	        case "left": player.setLeft(false); break;
	        case "jump": player.setJumping(false); break;
	        case "right": player.setRight(false); break;
	        default: throw new IllegalArgumentException("The player can't do this movement!");
    	}
	}
	
}
