package controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Config;

public abstract class AbstractController implements Controller {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    protected FXMLLoader fxmlLoader;
    
    public AbstractController(String viewFile) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewFile));
    	Parent root;
    	fxmlLoader.setController(this);
    	root = fxmlLoader.load();
    	
    	this.scene = new Scene(root, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
    	
	}

    public void display(Stage stage) {
        this.stage = stage;

        stage.setScene(scene);
        stage.setWidth(Config.WINDOW_WIDTH);
        stage.setHeight(Config.WINDOW_HEIGHT);
        stage.show();
    }
}