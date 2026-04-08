package Transitions;

import javafx.animation.PauseTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ChangeDiceNumber implements ITransitionStrategy {

    private final ImageView[] diceImages = new ImageView[6];
    private final ImageView dice1;
    private final ImageView dice2;
    private int currentIndex = 0;

    //Final 2 dice numbers
    private final int finalDie1;
    private final int finalDie2;

    public ChangeDiceNumber(ImageView image1, ImageView image2, ImageView image3,
                            ImageView image4, ImageView image5, ImageView image6,
                            ImageView dice1, ImageView dice2,
                            int finalDie1, int finalDie2) {
        this.diceImages[0] = image1;
        this.diceImages[1] = image2;
        this.diceImages[2] = image3;
        this.diceImages[3] = image4;
        this.diceImages[4] = image5;
        this.diceImages[5] = image6;
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.finalDie1 = finalDie1;
        this.finalDie2 = finalDie2;
    }

    @Override
    public void run() {
        currentIndex++;

        if (currentIndex < 6) {
            //Delayed 2 dice animation
            dice1.setImage(diceImages[currentIndex % 6].getImage());

            dice2.setImage(diceImages[(currentIndex + 2) % 6].getImage());

            PauseTransition pause = new PauseTransition(Duration.millis(150));
            pause.setOnFinished(e -> run());
            pause.play();
        } else {
            //Final numbers

            dice1.setImage(diceImages[finalDie1 - 1].getImage());
            dice2.setImage(diceImages[finalDie2 - 1].getImage());
        }
    }
}