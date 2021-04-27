package file;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

import main.Config.BlockType;
import main.Config.TileType;

public class MapWriterReader implements WriterReader {
	
	private int mapHeight;
	private int mapWidth;
	private String textInput;
	
	public String read(String fileName) throws IOException {
        try (var reader = new FileReader(fileName)) {
        	return read(reader);
        }
	}
	
	public String read(InputStreamReader reader) {
		textInput = "";
		
		Scanner s = new Scanner(reader);
		
		mapWidth = Integer.parseInt(s.nextLine());
		mapHeight = Integer.parseInt(s.nextLine());
	    
	    while (s.hasNext()){
	    	textInput += s.nextLine() + "\n";
	    }
	    
		s.close();
		
		return textInput;
	}
	
	public void write(String fileName, String text) throws IOException {	
		try (var writer = new PrintWriter(fileName);) {
			write(writer, text);
		} 
	}
	
	public void write(PrintWriter writer, String text) {
		this.textInput = text;
		
		if (textInput.charAt(textInput.length() - 1) != '\n') {
			textInput += '\n';
		}
		
		mapWidth = textInput.split("\n")[0].length();
		mapHeight = textInput.split("\n").length;
		
		validateMap();
		
		writer.print(mapWidth + "\n");
		writer.print(mapHeight + "\n");
		writer.printf(textInput);
		writer.flush();
		writer.close();
	}
	
	
	public void validateMap() {
		if ((mapWidth + 1) * mapHeight != textInput.length()) {
			throw new IllegalArgumentException("Map must be in a x*y format!\nTry to remove excess linebreaks");
		}
		
		char[] tileChars = textInput.toCharArray();
		char[] floorChars = textInput.split("\n")[mapHeight - 1].toCharArray();
		int playerCount = 0;
		int coinCount = 0;
		
		for (char c: tileChars)
		{
		    if (TileType.getValue(c) == null && c != '\n') {
		    	throw new IllegalArgumentException("Illegal tile(s)!");
		    }
		    if (TileType.getValue(c) == TileType.PLAYER) {
		    	playerCount += 1;
		    }
		    if (TileType.getValue(c) == TileType.COIN) {
		    	coinCount += 1;
		    }
		}
		
		if (playerCount != 1 || coinCount <= 0) throw new IllegalArgumentException("Map must have 1 player and at least 1 coin");;

		for (char c: floorChars)
		{
		    if (TileType.getValue(c).getType() != BlockType.SOLID && TileType.getValue(c).getType() != BlockType.DANGER && c != '\n') {
		    	throw new IllegalArgumentException("Map must have a solid/lava floor!");
		    }
		}
	}
	
	
	public int getMapHeight() {
		return mapHeight;
	}

	public int getMapWidth() {
		return mapWidth;
	}
}
