package Transitions;

import MediaClass.PlayNewMedia;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class DominoTransitions implements ITransitionStrategy {

	private final ImageView domino;
	private int currentPosition;
	private int stepsRemaining;
	private boolean moveHorizontal;

	//Domino tiles
	private final double[] positions = {0, 0, -115, -89, -89, -89, -89, -94, -89, -89,
			-89, -123, -92, -68, -66, -69, -65, -70, -65, -70, -68, -92,
			115, 89, 89, 89, 89, 94, 89, 89, 89, 123,
			92, 68, 66, 69, 65, 70, 65, 70, 68, 92};

	private final PlayNewMedia player = new PlayNewMedia("/sound/Domino Jumping1.mp3");

	public DominoTransitions(ImageView domino, int currentPosition, int steps) {
		this.domino = domino;
		this.currentPosition = currentPosition;
		this.stepsRemaining = steps;
	}

	@Override
	public void run() {
		if (stepsRemaining <= 0) return;


		int moveIndex = (currentPosition == 40) ? 41 : currentPosition + 1;


		moveHorizontal = (moveIndex >= 2 && moveIndex <= 11) || (moveIndex >= 22 && moveIndex <= 31);

		TranslateTransition trans = new TranslateTransition(Duration.millis(200), domino);
		if (moveHorizontal) {
			trans.setByX(positions[moveIndex]);
			trans.setByY(0);
		} else {
			trans.setByX(0);
			trans.setByY(positions[moveIndex]);
		}

		trans.play();
		try { player.run(); } catch (Exception ignored) {}


		currentPosition = (currentPosition % 40) + 1;
		stepsRemaining--;

		if (stepsRemaining > 0) {
			PauseTransition pause = new PauseTransition(Duration.millis(200));
			pause.setOnFinished(e -> run());
			pause.play();
		}
	}
}