package entities;

import javafx.scene.canvas.GraphicsContext;
import main.Config;
import rendering.TileMap;

public class Drop extends MapEntity {
	
	public Drop(TileMap tm, int initx, int inity) {
		super(tm, initx, inity);
		
		width = height = 30;
		collisionWidth = collisionHeight = 15;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		stopJumpSpeed = 0.3;
		imagePath = Config.DROP_IMAGE_PATH;
	}
	
	public void update() {
		if (falling) {
			dy += fallSpeed;
			
			if (dy > 0) jumping = false;
			if (dy < 0 && !jumping) dy += stopJumpSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
		}
		if (dy == 0) {
			setPosition();
		}
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}
	

	public void draw(GraphicsContext g) {
		setMapPosition();
		g.drawImage(image, 0, 0, 21, 21, (double) (x + xmap - (width - collisionWidth) / 2),
				(double) (y + ymap - height / 2), width, height);
	}
}
