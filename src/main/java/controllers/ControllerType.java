package controllers;

import javafx.stage.Stage;
import main.Config;

import java.io.IOException;
import java.util.EnumMap;

public enum ControllerType {

    MENU,
    OPTIONS;

    public static final EnumMap<ControllerType, Controller> CONTROLLER = new EnumMap<>(ControllerType.class);
    
    static {
        try {
			CONTROLLER.put(ControllerType.MENU, new MenuController(Config.MENU_VIEW_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
        	CONTROLLER.put(ControllerType.OPTIONS, new OptionsController(Config.OPTIONS_VIEW_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static void switchScreenTo(ControllerType type, Stage stage) {
        CONTROLLER.get(type).display(stage);
    }
    
    // Makes sure that level is reloaded on every entry. This method will also be necessary if multiple levels are added
    public static void loadLevel(Stage stage) {
        Controller levelScreen;
		try {
			levelScreen = new LevelController(Config.LEVEL_VIEW_PATH);
			levelScreen.display(stage);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}