package main;

import controllers.ControllerType;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

    	// Java FX stage setup
    	stage.setTitle("Platformer game");
    	stage.setResizable(false);
    	stage.setWidth(Config.WINDOW_WIDTH);
    	stage.setHeight(Config.WINDOW_HEIGHT);
    	stage.centerOnScreen();

        // Handle screen close
    	stage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            System.exit(0);
            stage.close();
        });

        // Change view to MENU
        ControllerType.switchScreenTo(ControllerType.MENU, stage);
    }
}