package entities;

import javafx.scene.canvas.GraphicsContext;
import main.Config;
import rendering.TileMap;

public class Worm extends MapEntity {

	public Worm(TileMap tm, int initx, int inity) {
		super(tm, initx, inity);
		// TODO Auto-generated constructor stub
		width = 60;
		height = 60;
		collisionWidth = 25;
		collisionHeight = 29;
		moveSpeed = 2;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		imagePath = Config.CHARACTER_IMAGE_PATH;
	}
	
	private void getNextPosition() {
		if (dx == 0) {
			moveSpeed = -moveSpeed;
			facingRight = !facingRight;
		}
		
		dx = moveSpeed;
		
		if (falling) {
			dy += fallSpeed;

			if (dy < 0) dy += stopJumpSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
	}

	@Override
	public void draw(GraphicsContext g) {
		setMapPosition();
		
		if (facingRight) {
			g.drawImage(image, 0, 96, 32, 32, (double) (x + xmap - (width - collisionWidth) / 2),
					(double) (y + ymap - height + 15), width, height);
		} else {
			g.drawImage(image, 0, 96, 32, 32, (double) (x + xmap - (width - collisionWidth) / 2) + width,
					(double) (y + ymap - height + 15), -width, height);
		}
	}
}
