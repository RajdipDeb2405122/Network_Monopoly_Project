package FXML_Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;

import MediaClass.PlayNewMedia;
import application.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Settings implements Initializable {


    @FXML private JFXSlider slider_music;
    @FXML private JFXSlider slider_sfx;
    @FXML private Label label_music_value;
    @FXML private Label label_sfx_value;
    @FXML private JFXTextField textfield_player_name;
    @FXML private Button button_save;
    @FXML private Button button_close;


    //Initialize
    public static double musicVolume = 0.5;
    public static double sfxVolume   = 0.5;
    public static String playerName  = "";



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        slider_music.setValue(musicVolume * 100);
        slider_sfx.setValue(sfxVolume * 100);


        label_music_value.setText((int)(musicVolume * 100) + "%");
        label_sfx_value.setText((int)(sfxVolume   * 100) + "%");


        if (playerName != null && !playerName.isEmpty()) {
            textfield_player_name.setText(playerName);
        }



        slider_music.valueProperty().addListener((obs, oldVal, newVal) -> {
            label_music_value.setText(newVal.intValue() + "%");

            applyMusicVolume(newVal.doubleValue() / 100.0);
        });

        slider_sfx.valueProperty().addListener((obs, oldVal, newVal) -> {
            label_sfx_value.setText(newVal.intValue() + "%");

            sfxVolume = newVal.doubleValue() / 100.0;
        });
    }



    @FXML
    void onMusicVolumeDragged(MouseEvent event) {
        applyMusicVolume(slider_music.getValue() / 100.0);
        label_music_value.setText((int) slider_music.getValue() + "%");
    }


    @FXML
    void onMusicVolumeChanged(MouseEvent event) {
        musicVolume = slider_music.getValue() / 100.0;
        applyMusicVolume(musicVolume);
        label_music_value.setText((int) slider_music.getValue() + "%");
        System.out.println("Music volume committed: " + (int)(musicVolume * 100) + "%");
    }


    @FXML
    void onSFXVolumeDragged(MouseEvent event) {
        sfxVolume = slider_sfx.getValue() / 100.0;
        label_sfx_value.setText((int) slider_sfx.getValue() + "%");
    }


    @FXML
    void onSFXVolumeChanged(MouseEvent event) {
        sfxVolume = slider_sfx.getValue() / 100.0;
        label_sfx_value.setText((int) slider_sfx.getValue() + "%");
        System.out.println("SFX volume committed: " + (int)(sfxVolume * 100) + "%");


        new PlayNewMedia("/sound/hover_new.mp3").run();
    }


    @FXML
    void ClickedOnSave(MouseEvent event) {


        musicVolume = slider_music.getValue() / 100.0;
        sfxVolume   = slider_sfx.getValue()   / 100.0;
        playerName  = textfield_player_name.getText().trim();


        applyMusicVolume(musicVolume);

        System.out.println("Settings saved:");
        System.out.println("  🎵 Music : " + (int)(musicVolume * 100) + "%");
        System.out.println("  🔊 SFX   : " + (int)(sfxVolume   * 100) + "%");
        System.out.println("  👤 Name  : " + (playerName.isEmpty() ? "(default)" : playerName));

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Settings Saved");
        alert.setHeaderText(null);
        alert.setContentText(
                "Settings saved!\n\n" +
                        "🎵 Music Volume : " + (int)(musicVolume * 100) + "%\n" +
                        "🔊 SFX Volume   : " + (int)(sfxVolume   * 100) + "%\n" +
                        "👤 Player Name  : " + (playerName.isEmpty() ? "(default)" : playerName)
        );
        alert.showAndWait();

        closeWindow();
    }

    @FXML
    void MouseEnteredOnSave(MouseEvent event) {
        new PlayNewMedia("/sound/hover_new.mp3").run();
    }


    @FXML
    void ClickedOnClose(MouseEvent event) {

        applyMusicVolume(musicVolume);

        closeWindow();
    }

    @FXML
    void MouseEnteredOnClose(MouseEvent event) {
        new PlayNewMedia("/sound/hover_new.mp3").run();
    }


    private void applyMusicVolume(double volume) {
        if (Main.backgroundMusic != null) {
            Main.backgroundMusic.setVolume(volume);
        }
    }


    private void closeWindow() {
        ((Stage) button_close.getScene().getWindow()).close();
    }
}