package rendering;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public abstract class AnimationTimer {
	private Timeline animation;
	
	public void initTimer(int FPS) {
		KeyFrame frame = new KeyFrame(Duration.millis(1000 / FPS), e -> loop());
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
	}
	
	public void startTimer() {
		animation.play();
	}
	
	public void stopTimer() {
		animation.pause();
	}
	
	public abstract void loop();
}
