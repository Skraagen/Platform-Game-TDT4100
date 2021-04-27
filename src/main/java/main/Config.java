package main;

public class Config {
	public static final int WINDOW_WIDTH = 1200;
	public static final int WINDOW_HEIGHT = 600;
	public static final int GAME_TOOLBAR_HEIGHT = 40;
	public static final int FPS = 80;
	public static final String TILE_MAP_PATH = "mainmap.txt";

	public static final int PLAYER_HEALTH = 3;
	public static final String CHARACTER_IMAGE_PATH = "images/characters.png";
	public static final String COIN_IMAGE_PATH = "images/coin.gif";
	public static final String DROP_IMAGE_PATH = "images/drop.png";
	public static final String TILESET_IMAGE_PATH = "images/sheet2.png";
	
	public static final String LEVEL_VIEW_PATH = "/views/LevelView.fxml";
	public static final String MENU_VIEW_PATH = "/views/MenuView.fxml";
	public static final String OPTIONS_VIEW_PATH = "/views/OptionsView.fxml";
	
	public static enum BlockType {
		SOLID,
		CLEAR,
		DANGER,
	}
	
	public static enum TileType {
		// TYPE (token, tilesetX, tilsetY, BlockType)
	    AIR('.', 0, 0, BlockType.CLEAR),
	    FLOWER1('1', 0, 1, BlockType.CLEAR),
	    FLOWER2('2', 0, 2, BlockType.CLEAR),
	    ROCK('3', 0, 3, BlockType.CLEAR),
	    LAVA_SURFACE('4', 0, 4, BlockType.DANGER),
	    GRASS_LEFT('5', 1, 0, BlockType.SOLID),
	    GRASS_FULL('6', 1, 1, BlockType.SOLID),
	    GRASS_RIGHT('7', 1, 2, BlockType.SOLID),
	    GROUND('8', 1, 3, BlockType.SOLID),
	    LAVA_GROUND('9', 1, 4, BlockType.DANGER),
	    // Entities that are not connected to the tilset
		PLAYER('X', BlockType.CLEAR),
		DROP('D', BlockType.CLEAR),
		WORM('O', BlockType.CLEAR),
		COIN('C', BlockType.CLEAR)
		;
		
		private final char token;
		private final int tilesetX;
		private final int tilesetY;
		private final BlockType type;
	
		TileType(char token, int tilesetY, int tilesetX, BlockType type) {
			this.token = token;
			this.tilesetX = tilesetX;
			this.tilesetY = tilesetY;
			this.type = type;
		}
		
		TileType(char token, BlockType type) {
			this.token = token;
			this.tilesetX = 0;
			this.tilesetY = 0;
			this.type = type;
		}
	
		public static TileType getValue(char token) {;
		  for(TileType e: TileType.values()) {
		    if(e.token == token) {
		      return e;
		    }
		  }
		  return null;
		}
		
		public char getToken() {
			return token;
		}
	
		public int getTilesetX() {
			return tilesetX;
		}
	
		public int getTilesetY() {
			return tilesetY;
		}
	
		public BlockType getType() {
			return type;
		}
	}
	
	
}
