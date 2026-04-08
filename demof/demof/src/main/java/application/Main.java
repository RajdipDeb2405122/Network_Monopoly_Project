package application;

import Rules_Of_The_game.PlayerArea;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Main extends Application {

	//Initial
	public static int players_number = 2;
	public static int players_amount = 1500;

	public static int player_turn = 0;
	public static int static_player_place = 0;
	public static int Building_price = 0;

	//Popup
	public static Label Static_PopUp_Label = new Label("");

	//Owned image
	public static ImageView[] images_owned_buildings = new ImageView[41]; // 1–40

	//Players
	public static PlayerArea Gamer1 = new PlayerArea();
	public static PlayerArea Gamer2 = new PlayerArea();
	public static PlayerArea Gamer3 = new PlayerArea();
	public static PlayerArea Gamer4 = new PlayerArea();
	public static PlayerArea Gamer5 = new PlayerArea();
	public static PlayerArea Gamer6 = new PlayerArea();
	public static PlayerArea Gamer7 = new PlayerArea();
	public static PlayerArea Gamer8 = new PlayerArea();

//	public static Parent root;
//	public static Stage primaryStage;
	public static MediaPlayer backgroundMusic;


    //Start
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/FXML_files/FirstPage.fxml"));

		//Screen resize
        javafx.geometry.Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();


        double scaleFactor = Math.min(1.0, bounds.getHeight() / 700.0);


        javafx.scene.transform.Scale scale = new javafx.scene.transform.Scale(scaleFactor, scaleFactor, 0, 0);
		root.getTransforms().add(scale);


        Scene scene = new Scene(root, 600 * scaleFactor, 700 * scaleFactor);



		primaryStage.setTitle("Monopoly - Main Menu");
		primaryStage.setScene(scene);


        primaryStage.show();


        primaryStage.setX((bounds.getWidth() - primaryStage.getWidth()) / 2);
		primaryStage.setY((bounds.getHeight() - primaryStage.getHeight()) / 2);
		startBackgroundMusic();
	}



	public static void startBackgroundMusic() {
		try {
			String path = Main.class.getResource("/sound/Background.mp3").toString();
			Media media = new Media(path);
			backgroundMusic = new MediaPlayer(media);
			backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
			backgroundMusic.setVolume(0.5);
			backgroundMusic.play();
		} catch (Exception e) {
			System.out.println("Background music not found: " + e.getMessage());
		}
	}



	public static void main(String[] args) {
		launch(args);
	}
}