package rendering;

import javafx.scene.image.Image;
import main.Config.TileType;
import main.Config.BlockType;

public class Tile {
	private TileType type;
	private Image image;
	private int sheetX;
	private int sheetY;
	
	public Tile(TileType type) {
		this.type = type;
		
		sheetX = this.type.getTilesetX() * 16;
		sheetY = this.type.getTilesetY() * 16;
	}
	
	public Image getImage() { return image; }
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public BlockType getType() {
		return type.getType();
	}
	
	public int getSheetX() {
		return sheetX;
	}
	
	public int getSheetY() {
		return sheetY;
	}
}
