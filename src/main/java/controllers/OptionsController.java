package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.Config;
import main.Config.TileType;
import java.io.*;
import file.MapWriterReader;

public class OptionsController extends AbstractController {
	
	@FXML
    private TextArea textArea;
    
    @FXML
    private Label width, height, description;
    
    @FXML
    private Button menu;
    
    private Stage stage;
	private MapWriterReader mapEditor;
    
    public OptionsController(String viewFile) throws IOException {
		super(viewFile);
		
		 displayTokens();
	}
    
    public void display(Stage stage) {
    	mapEditor = new MapWriterReader();
    	
    	try {
			textArea.setText(mapEditor.read(Config.TILE_MAP_PATH));
		} catch (Exception e) {
			String[] text = e.toString().split(": ");
    		Alert errorDialog = new Alert(AlertType.ERROR);
    		errorDialog.setHeaderText(text[0]);
    		errorDialog.setContentText(text[1]);
    		errorDialog.showAndWait();
		}
    	
    	updateLabels();
    	
    	this.stage = stage;
    	stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void exit() {
        ControllerType.switchScreenTo(ControllerType.MENU, stage);
    }

    @FXML
    private void save() {
    	try {
	    	mapEditor.write(Config.TILE_MAP_PATH, textArea.getText());
	    	updateLabels();
	    	
	    	Alert confirmationDialog = new Alert(AlertType.INFORMATION);
	    	confirmationDialog.setHeaderText("Map saved");
	    	confirmationDialog.showAndWait();
    	} catch (Exception e) {
    		String[] text = e.toString().split(": ");
    		Alert errorDialog = new Alert(AlertType.ERROR);
    		errorDialog.setHeaderText(text[0]);
    		errorDialog.setContentText(text[1]);
    		errorDialog.showAndWait();
    	}
    }
    
    private void updateLabels() {
    	width.setText("Horizontal map tiles: " + Integer.toString(mapEditor.getMapWidth()));
    	height.setText("Vertical map tiles: " + Integer.toString(mapEditor.getMapHeight()));
    }
    
    private void displayTokens() {
    	for (TileType tile : TileType.values()) {
    		description.setText(description.getText() + "\n" + tile.getToken() + " - " + tile);
    	}
    }
}