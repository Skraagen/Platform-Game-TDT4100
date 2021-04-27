package entities;

import javafx.scene.canvas.GraphicsContext;
import main.Config;
import rendering.TileMap;

public class Coin extends MapEntity {
	public Coin(TileMap tm, int initx, int inity) {
		super(tm, initx, inity);
		
		width = height = 40;
		collisionWidth = collisionHeight = 15;	
		imagePath = Config.COIN_IMAGE_PATH;
	}
	
	public void update() {	
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}
	
	public void draw(GraphicsContext g) {
		setMapPosition();
		g.drawImage(image, 0, 0, 400, 400, (double) (x + xmap - (width - collisionWidth) / 2),
				(double) (y + ymap - height / 2), width, height);
	}
}
