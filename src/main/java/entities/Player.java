package entities;

import javafx.scene.canvas.GraphicsContext;
import main.Config;
import rendering.TileMap;

public class Player extends MapEntity {
	
	private int health;
	private final int maxHealth;
	private int coins;
	private boolean dead;

	public Player(TileMap tm, int initx, int inity) {
		super(tm, initx, inity);
		
		width = 50;
		height = 50;
		collisionWidth = 25;
		collisionHeight = 25;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5.6;
		stopJumpSpeed = 0.3;
		imagePath = Config.CHARACTER_IMAGE_PATH;
		health = maxHealth = Config.PLAYER_HEALTH;
	}

	public int getHealth() {return health; }
	public int getMaxHealth() {return maxHealth; }
	public void setHealth(int health) { this.health = health;}
	public int getCoins() { return coins; }
	public void addCoin() { coins++; }

	private void getNextPosition() {
		if (left) {
			facingRight = false;
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if (right) {
			facingRight = true;
			dx += moveSpeed;
			if( dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			}
			else if (dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
			
		}
		
		if (falling) {
			dy += fallSpeed;
			
			if (dy > 0) jumping = false;
			if (dy < 0 && !jumping) dy += stopJumpSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}
	

	public void draw(GraphicsContext g) {
		setMapPosition();

		if (facingRight) {
			g.drawImage(image, 0, 0, 32, 32, (double) (x + xmap - (width - collisionWidth) / 2),
					(double) (y + ymap - height + 12), width, height);
		} else {
			g.drawImage(image, 0, 0, 32, 32, (double) (x + xmap - (width - collisionWidth) / 2) + width,
					(double) (y + ymap - height + 12), -width, height);
		}
		
		// For collision debugging
		//g.strokeRect((double) (x + xmap), (double) (y + ymap), (double)collisionWidth, (double)collisionHeight);
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
}
