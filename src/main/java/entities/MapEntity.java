package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import main.Config.BlockType;
import rendering.TileMap;

public abstract class MapEntity {
	// Variables related to tilemap
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap, ymap;
	
	// Positioning
	protected double x, y, dx, dy;
	protected final double initialX, initialY;
	
	// Size
	protected double width, height;
	protected double collisionWidth, collisionHeight;
	
	protected int currentTileRow, currentTileCol;
	
	// Temporary positions
	protected double xdestination, ydestination;
	protected double xtemp, ytemp;
	
	// Tile collisions
	protected boolean topLeft, topRight, bottomLeft, bottomRight;
	protected boolean isCollidingWithDangerBlock;
	
	protected boolean facingRight = true;
	
	// Actions
	protected boolean left, right, up, down, jumping, falling;
	protected double moveSpeed, maxSpeed, stopSpeed, fallSpeed, maxFallSpeed, jumpStart, stopJumpSpeed;
	
	protected Image image;
	protected String imagePath;
	
	public MapEntity(TileMap tm, int initialX, int initialY) {
		tileMap = tm;
		tileSize = tm.getTileSize();
		this.initialX = initialX;
		this.initialY = initialY;
		
		setPosition(initialX, initialY); // Set initial position
	}
	
	// Check collision with inputentity
	public boolean intersects(MapEntity o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		
		return r1.intersects(r2.getBoundsInLocal());
	}
	
	// Create collisionshape for intersection check
	protected Rectangle getRectangle() {
		return new Rectangle(
			(int) (x),
			(int) (y - collisionHeight),
			(int) (collisionWidth),
			(int) (collisionHeight)
		);
	}
	
	// Calculate if cornertile is solid or dangerous. Set collisionstate of entity
	public void calculateCorners(double x, double y) {
		// Find tiles that are collided with
		int leftTile = (int) (x) / tileSize;
		int rightTile = (int) (x + collisionWidth) / tileSize;
		int topTile = (int) (y - collisionHeight / 2) / tileSize;
		int bottomTile = (int) (y + collisionHeight / 2 - 1) / tileSize;
		
		// Get type of cornertiles
		BlockType tl = tileMap.getType(topTile, leftTile);
		BlockType tr = tileMap.getType(topTile, rightTile);
		BlockType bl = tileMap.getType(bottomTile, leftTile);
		BlockType br = tileMap.getType(bottomTile, rightTile);
		
		if (x < 0) {
			bl = BlockType.SOLID;
			tl = BlockType.SOLID;
		}

		// Set true if colliding with solid block
		topLeft = tl == BlockType.SOLID;
		topRight = tr == BlockType.SOLID;
		bottomLeft = bl == BlockType.SOLID;
		bottomRight = br == BlockType.SOLID;
		
		// Set true if colliding with dangerous block
		isCollidingWithDangerBlock = (bl == BlockType.DANGER || br == BlockType.DANGER);
	}
	
	// Check if entity collides with map and set state accordingly
	public void checkTileMapCollision() {
		// Current col/row coordinates of the entity
		currentTileCol = (int)x / tileSize;
		currentTileRow = (int)y / tileSize;
		// Coordinates where the entity wants to move
		xdestination = x + dx;
		ydestination = y + dy;
		// Temporary coordinates for calculation
		xtemp = x;
		ytemp = y;
		
		// Set collision state in y-direction
		calculateCorners(x, ydestination);
		
		// Set position values according to collision
		if (dy < 0) {
			if (topLeft || topRight) {
				dy = 0;
				ytemp = currentTileRow * tileSize + collisionHeight / 2;
			} else {
				ytemp += dy;
			}
		}
		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currentTileRow + 1) * tileSize - collisionHeight / 2;
			} else {
				ytemp += dy;
			}
		}
		
		// Set collision state in x-direction
		calculateCorners(xdestination, y);
		
		// Set position values according to collision
		if (dx < 0) {
			if (topLeft || bottomLeft) {
				dx = 0;
				xtemp = currentTileCol * tileSize;
			} else {
				xtemp += dx;
			}
		}
		if (dx > 0) {
			if (bottomRight || topRight) {
				dx = 0;
				xtemp = currentTileCol * tileSize - (collisionWidth - tileSize) - 0.5;
			} else {
				xtemp += dx;
			}
		}
		
		// Check if entity can fall and set falling-state accordingly
		if (!falling) {
			calculateCorners(x, ydestination + 1);
			
			if (!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}
	
	// Set entity position
	public void setPosition(double x, double y) {
		if (x >= xmap 
				&& x <= tileMap.getWidth() + collisionWidth
				&& y <= tileMap.getHeight() + collisionHeight) {
			this.x = x;
			this.y = y;
		} else {
			throw new IllegalArgumentException("Entity cannot be placed outside the map.");
		}
	}
	
	// Set entity position to initial values
	public void setPosition() {
		setPosition(initialX, initialY);
	}
	
	// Set map position values
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	// Seperated method to make testing easier
	public void loadImage() {
		try {
			image = new Image(getClass().getClassLoader().getResource(imagePath).toExternalForm());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Set direction
	public void setUp(boolean b) { up = b;}
	public void setDown(boolean b) { down = b;}
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setJumping(boolean b) { jumping = b;}
	
	// Get position
	public int getx() { return (int) x; }
	public int gety() { return (int) y; }
	
	// Get size
	public double getWidth() { return width; }
	public double getHeight() { return height; }
	public double getCWidth() { return collisionWidth; }
	public double getCHeight() { return collisionHeight; }
	
	public boolean isCollidingWithDangerBlock() { return isCollidingWithDangerBlock; }
	
	// Necessary abstracts for rendering
	public abstract void draw(GraphicsContext g);
	public abstract void update();
}
