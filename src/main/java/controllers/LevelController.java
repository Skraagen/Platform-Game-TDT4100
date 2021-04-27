package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import listeners.LevelListener;
import main.Config;
import rendering.TileMapRenderer;

public class LevelController extends AbstractController implements LevelListener {

	@FXML
    private Button quit, play, exit;
    
    @FXML
    private Canvas canvas;
    
    @FXML
    private Label health, coins, endPaneLabel;
    
    @FXML
    private AnchorPane endPane;
    
    @FXML
    private VBox container;
    
    @FXML
    private ToolBar toolbar;
    
    @FXML
    private ImageView winImage;
    
    private Stage stage;
    
	private TileMapRenderer levelRenderer;
	
	public LevelController(String viewFile) throws IOException {
		super(viewFile);
	}

    public void display(Stage stage) {
    	canvas.setWidth(Config.WINDOW_WIDTH);
    	canvas.setHeight(Config.WINDOW_HEIGHT - Config.GAME_TOOLBAR_HEIGHT);
    	toolbar.setPrefHeight(Config.GAME_TOOLBAR_HEIGHT);
    	endPane.setPrefSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
    	container.setPrefSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
    	toolbar.setPrefWidth(Config.WINDOW_WIDTH);
        canvas.setFocusTraversable(true);
        endPane.setVisible(false);
        winImage.setVisible(false);

        try {
			levelRenderer = new TileMapRenderer(canvas);
		} catch (IOException e) {
			e.printStackTrace();
		}
        levelRenderer.addLevelRendererListener(this);
        levelRenderer.startTimer();
        
        health.setText("Health: " + Integer.toString(levelRenderer.getPlayerMaxHealth()) + " / " + Integer.toString(levelRenderer.getPlayerMaxHealth()));
        coins.setText("Coins picked: 0 / " + Integer.toString(levelRenderer.getTotalCoins()));
        
        attachKeyHandler();
        
        this.stage = stage;

        stage.setScene(scene);
        stage.setWidth(Config.WINDOW_WIDTH);
        stage.setHeight(Config.WINDOW_HEIGHT);
        stage.show();
    }
    
    @FXML
    private void quit() {
    	levelRenderer.stopTimer();
    	ControllerType.switchScreenTo(ControllerType.MENU, stage);
    }
    
    @FXML
    private void play() {
    	ControllerType.loadLevel(stage);
    }
    
    public void playerDead(int playerHealth) {
    	health.setText("Health: " + Integer.toString(playerHealth) + " / " + Integer.toString(levelRenderer.getPlayerMaxHealth()));
    };
	
	public void playerCollect(int playerCoins) {
		coins.setText("Coins picked: " + Integer.toString(playerCoins) + " / " + levelRenderer.getTotalCoins());
	};
	
	public void playerWin() {
		endPaneLabel.setText("Congratulations! You won!");
		endPane.setVisible(true);
		winImage.setVisible(true);
		levelRenderer.stopTimer();
	};
	
	public void playerLose() {
		endPaneLabel.setText("I'm sorry. You lost.");
		endPane.setVisible(true);
		levelRenderer.stopTimer();
	};
    
    private void attachKeyHandler() {
	    scene.setOnKeyPressed(keyEvent -> {
	    	switch (keyEvent.getCode()) {
	            case A: levelRenderer.movePlayer("left"); break;
	            case SPACE: levelRenderer.movePlayer("jump"); break;
	            case D: levelRenderer.movePlayer("right"); break;
	            case Q: System.exit(0); this.stage.close(); break;
	            case M: levelRenderer.stopTimer(); ControllerType.switchScreenTo(ControllerType.MENU, stage); break;
				default: break;
	        }
	        
        });
	    
        scene.setOnKeyReleased(keyEvent -> {
        	switch (keyEvent.getCode()) {
	            case A:   levelRenderer.stopMovePlayer("left"); break;
	            case SPACE:  levelRenderer.stopMovePlayer("jump"); break;
	            case D:   levelRenderer.stopMovePlayer("right"); break;
				default: break;
            }
        });
    }
}
