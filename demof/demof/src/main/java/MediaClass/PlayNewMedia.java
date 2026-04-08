package MediaClass;

import Transitions.ITransitionStrategy;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PlayNewMedia implements ITransitionStrategy {

	private final String path;


	public PlayNewMedia() {
		this.path = null;
	}


	public PlayNewMedia(String path) {
		this.path = path;
	}

	@Override
	public void run() {
		if (path == null || path.trim().isEmpty()) {
			System.out.println("Media path is null or empty.");
			return;
		}

		try {
			String resourcePath = getClass().getResource(path).toString();
			if (resourcePath == null) {
				System.out.println("Media file not found: " + path);
				return;
			}

			Media media = new Media(resourcePath);
			MediaPlayer mediaPlayer = new MediaPlayer(media);



			mediaPlayer.setVolume(FXML_Controllers.Settings.sfxVolume);

			mediaPlayer.play();
			mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.dispose());

		} catch (Exception e) {
			System.out.println("Error playing media: " + path);
			e.printStackTrace();
		}
	}
}