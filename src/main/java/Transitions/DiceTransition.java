package Transitions;

import MediaClass.PlayNewMedia;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class DiceTransition implements ITransitionStrategy {

	private final ImageView image1;
	private final ImageView image2;
	private final int clickNumber;
	private final PlayNewMedia player;

	public DiceTransition(ImageView image1, ImageView image2, int clickNumber) {
		this.image1 = image1;
		this.image2 = image2;
		this.clickNumber = clickNumber;
		this.player = new PlayNewMedia("/sound/Dice Rolling.mp3");
	}

	@Override
	public void run() {

		RotateTransition rt1 = createRotation(image1);
		RotateTransition rt2 = createRotation(image2);

		//Shake/Bounce
		TranslateTransition tt1 = new TranslateTransition(Duration.millis(300), image1);
		TranslateTransition tt2 = new TranslateTransition(Duration.millis(300), image2);
		setupMovement(tt1, tt2);


		ParallelTransition parallel = new ParallelTransition(rt1, rt2, tt1, tt2);
		parallel.play();


		player.run();
	}

	private RotateTransition createRotation(ImageView target) {
		RotateTransition rt = new RotateTransition(Duration.millis(300), target);
		rt.setAxis(Rotate.Z_AXIS);
		rt.setByAngle(360);
		rt.setCycleCount(2);
		rt.setAutoReverse(true);
		return rt;
	}

	private void setupMovement(TranslateTransition t1, TranslateTransition t2) {
		t1.setCycleCount(2);
		t1.setAutoReverse(true);
		t2.setCycleCount(2);
		t2.setAutoReverse(true);

		if (clickNumber % 2 == 0) {
			t1.setByX(50); t1.setByY(-50);
			t2.setByX(-50); t2.setByY(50);
		} else {
			t1.setByX(-50); t1.setByY(50);
			t2.setByX(50); t2.setByY(-50);
		}
	}
}
