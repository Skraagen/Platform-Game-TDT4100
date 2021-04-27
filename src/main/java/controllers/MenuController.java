package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Config;

public class MenuController extends AbstractController {

		@FXML
	    private Button game, options, quit;
	    
	    @FXML
	    private VBox container;
	    
	    private Stage stage;
	    
	    public MenuController(String viewFile) throws IOException {
			super(viewFile);
		}

		public void display(Stage stage) {
	        container.setPrefSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
	    	
	        this.stage = stage;

	        stage.setScene(scene);
	        stage.setWidth(Config.WINDOW_WIDTH);
	        stage.setHeight(Config.WINDOW_HEIGHT);
	        stage.show();
	    }
	    
	    @FXML
	    public void game() {
	    	ControllerType.loadLevel(stage);
	    }
	    
	    @FXML
	    public void options() {
	    	ControllerType.switchScreenTo(ControllerType.OPTIONS, stage);
	    }
	    
	    @FXML
	    public void quit() {
	    	System.exit(0);
            stage.close();
	    }
}
