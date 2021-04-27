package rendering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import entities.Coin;
import entities.Drop;
import entities.MapEntity;
import entities.Player;
import entities.Worm;
import file.MapWriterReader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.Config;
import main.Config.TileType;
import main.Config.BlockType;

public class TileMap {
	
	// Map position
	private double x;
	private double y;
	
	// Map position boundaries. Makes sure the map always fills the screen
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	// Allow for smooth transitions in Y direction. Start value makes opening animation
	private double ytemp = -80;
	
	private int tileSize;
	
	// Number of rows and cols in tilemap
	private int numRows;
	private int numCols;
	
	// Width and height in pixels
	private int width;
	private int height;
	
	// Size of viewport
	private int windowHeight;
	private int windowWidth;
	
	// All entities in map
	private ArrayList<MapEntity> entities = new ArrayList<>();
	
	// All tiles in map
	private ArrayList<ArrayList<Tile>> tiles;
	
	// Tileset to be drawn
	private Image tileset;
	
	// Viewport offset, determines which tiles to draw
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	private MapWriterReader map;
	private String mapFilePath;
	
	public TileMap(String mapFilePath, int tileSize, int windowWidth, int windowHeight) {
		this.tileSize = tileSize;
		this.windowHeight = windowHeight;
		this.windowWidth = windowWidth;
		this.mapFilePath = mapFilePath;
		
		// Set number of tiles to draw on the screen at a given time. Adds 2 to make sure you don't see tile loading.
		numRowsToDraw = windowHeight / tileSize + 2;
		numColsToDraw = windowWidth / tileSize + 2;
	}
	
	// Convert the map into tiles and get all entities
	public void build() throws IOException {
		map = new MapWriterReader();
		String rows[] = map.read(mapFilePath).split("\\r?\\n");
		map.validateMap();
		
		numCols = map.getMapWidth();
		numRows = map.getMapHeight();
		
		width = numCols * tileSize;
		height = numRows * tileSize;
		
		// Set map drawing bounds
		xmin = windowWidth - width;
		xmax = 0;
		ymin = windowHeight - height - tileSize;
		ymax = Math.max(0, windowHeight - height); // If map is smaller than screen, always draw to bottom of screen
		
		tiles = new ArrayList<>();

		for(int row = 0; row < numRows; row++) {
			char[] tokens = rows[row].toCharArray(); // Split map-line to list of tokens
			
			tiles.add(new ArrayList<>());
			
			for (int col = 0; col < numCols; col++) {
				TileType tile = Config.TileType.getValue(tokens[col]); // Get associated tiletype
				
				// Add blocktiles
				tiles.get(row).add(new Tile(tile));
				
				// Add entities
				switch (tile) {
					case COIN: entities.add(new Coin(this, (int) x + (col) * tileSize, (int)y + row * tileSize));
						break; 
					case DROP: entities.add(new Drop(this, (int) x + (col) * tileSize, (int)y + row * tileSize));
						break;
					case PLAYER: entities.add(new Player(this, (int) x + (col) * tileSize, (int)y + row * tileSize));
						break;
					case WORM: entities.add(new Worm(this, (int) x + (col) * tileSize, (int)y + row * tileSize));
						break;
					default:
						break;
				}
			}
		}

	}
	
	// Get type of a specified block-coordinate in the map
	public BlockType getType(int row, int col) {
		if (col < 0 || col >= numCols) return BlockType.SOLID; // If x-coordinate is outside map
		if (row < 0 || row >= numRows) return BlockType.CLEAR; // If y-coordinate is outside map
		
		try { 
			return tiles.get(row).get(col).getType();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error happened at row: " + row + " and col: " + col);
		}
	}
	
	// Set map position
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		
		fixBounds(); // Fix map position
		
		// Set tiledrawing offsets
		colOffset = (int) - this.x / tileSize;
		rowOffset = (int) Math.max(0, - this.y / tileSize); // If the map is smaller than the canvas, set offset to 0
	}
	
	// Set max/min position values if player reaches map border
	private void fixBounds() {
		if (x < xmin) x = xmin;
		if (x > xmax) x = xmax;
		
		// Set the targeted y coordinate
		double ytarget = y;
		if (y < ymin) ytarget = ymin;
		if (y > ymax) ytarget = ymax;
		
		// Animate the screen towards that target y
		if (ytemp < ytarget) {
			ytemp += Math.pow(Math.abs(ytarget - ytemp), 2)/1500;
		} 
		if (ytemp > ytarget){
			ytemp -= Math.pow(Math.abs(ytarget - ytemp), 2)/1500;
		}
		
		y = ytemp;
	}
	
	// Seperated for easier method testing
	public void loadMapImage() {
		tileset = new Image(getClass().getClassLoader().getResource(Config.TILESET_IMAGE_PATH).toExternalForm());
	}
	
	// Draw visible tiles to canvas
	public void draw(GraphicsContext g) {
		
		for ( int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			
			if (row >= numRows) break;
			
			for ( int col = colOffset; col < colOffset + numColsToDraw; col++) {

				if (col >= numCols) break;
				
				//Add 0.5 to image width and height to fix ugly scaling
				g.drawImage(tileset, 
						tiles.get(row).get(col).getSheetX() + 0.5,
						tiles.get(row).get(col).getSheetY() + 0.5, 
						15, 
						15, 
						(double) x + col * tileSize, 
						(double) y + row * tileSize, 
						tileSize, 
						tileSize);
			}
		}
	}
	
	public Iterator<MapEntity> entityIterator() { return entities.iterator(); }
	public int getTileSize() { return tileSize; }
	public int getx() { return (int) x; }
	public int gety() { return (int) y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
}
