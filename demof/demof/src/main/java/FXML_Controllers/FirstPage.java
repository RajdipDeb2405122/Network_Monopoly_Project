package FXML_Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import MediaClass.PlayNewMedia;
import application.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FirstPage extends Main implements Initializable {


    @FXML private AnchorPane Center_AnchorPane;
    @FXML private AnchorPane AnchorPane_start;


    @FXML private Button button_start;
    @FXML private Button button_join;
    @FXML private Button button_settings;
    @FXML private Button button_help;
    @FXML private Button button_about;
    @FXML private Button button_exit;


    @FXML private Button button_back;
    @FXML private Button button_new;


    @FXML private ImageView image_body, image_header;
    @FXML private ImageView image_bar_select_player;
    @FXML private ImageView image_number_of_players;
    @FXML private ImageView image_select_1, image_select_2, image_select_3, image_select_4;
    @FXML private ImageView image_select_5, image_select_6, image_select_7, image_select_8;
    @FXML private ImageView image_triangle;
    @FXML private ImageView image_amount_label;
    @FXML private ImageView image_dollar_symbole;

    @FXML private JFXTextField textfield_amount;

    private PlayNewMedia player;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AnchorPane_start.setVisible(false);

        image_bar_select_player.setVisible(false);
        image_number_of_players.setVisible(false);
        image_triangle.setVisible(false);
        image_amount_label.setVisible(false);
        textfield_amount.setVisible(false);
        image_dollar_symbole.setVisible(false);

        Main.players_number = 2;
    }


    @FXML
    void clicked_Start(MouseEvent event) {
        new PlayNewMedia("/sound/click.mp3").run();
        player = new PlayNewMedia("/sound/clicked start.mp3");
        player.run();

        AnchorPane_start.setVisible(true);
        image_bar_select_player.setVisible(true);
        image_number_of_players.setVisible(true);
        image_triangle.setVisible(true);
        image_amount_label.setVisible(true);
        textfield_amount.setVisible(true);
        image_dollar_symbole.setVisible(true);
    }

    @FXML
    void MouseEnteredOnStart(MouseEvent event) {
        player = new PlayNewMedia("/sound/hover_new.mp3");
        player.run();
    }

    @FXML
    void MouseExitedOnStart(MouseEvent event) {}



    @FXML
    void ClickedOnJoin(MouseEvent event) throws java.io.IOException {
        new PlayNewMedia("/sound/click.mp3").run();
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog("localhost");
        dialog.setTitle("Join Multiplayer Game");
        dialog.setHeaderText("Enter the Host's IP Address:");
        dialog.setContentText("IP Address:");

        java.util.Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String ipAddress = result.get().trim();
            if (ipAddress.isEmpty()) ipAddress = "localhost";

            Networking.ClientNetworkManager.instance.connectToServer(ipAddress);

            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(
                    getClass().getResource("/FXML_files/SecondPage.fxml"));

            javafx.geometry.Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            double scaleFactor = Math.min(1.0, (bounds.getHeight() - 40) / 980.0);

            javafx.scene.transform.Scale scale = new javafx.scene.transform.Scale(scaleFactor, scaleFactor, 0, 0);
            root.getTransforms().add(scale);

            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(root, 1100 * scaleFactor, 980 * scaleFactor));
            stage.setTitle("Monopoly - Client");
            stage.show();
            stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((bounds.getHeight() - stage.getHeight()) / 2);

            try { new PlayNewMedia("/sound/Popup_SecondPage.mp3").run(); } catch (Exception e) {}

            ((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    @FXML
    void MouseEnteredOnJoin(MouseEvent event) {
        player = new PlayNewMedia("/sound/hover_new.mp3");
        player.run();
    }



    @FXML
    void ClickedOnSettings(MouseEvent event) {
        new PlayNewMedia("/sound/click.mp3").run();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/Settings.fxml"));
            Parent root = loader.load();

            Stage settingsStage = new Stage();
            settingsStage.setTitle("Settings");



            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.initOwner(button_settings.getScene().getWindow());

            Scene scene = new Scene(root, 500, 550);
            settingsStage.setScene(scene);
            settingsStage.setResizable(false);



            javafx.geometry.Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            settingsStage.setX((bounds.getWidth()  - 500) / 2);
            settingsStage.setY((bounds.getHeight() - 550) / 2);

            settingsStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not open Settings");
            alert.setContentText("Make sure Settings.fxml is in /FXML_files/");
            alert.showAndWait();
        }
    }

    @FXML
    void MouseEnteredOnSettings(MouseEvent event) {
        player = new PlayNewMedia("/sound/hover_new.mp3");
        player.run();
    }



    @FXML
    void ClickedOnHelp(MouseEvent event) {
        new PlayNewMedia("/sound/click.mp3").run();
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("How To Play Network Monopoly");
        alert.setContentText(
                "1. HOST clicks START, selects number of players and starting amount.\n\n" +
                        "2. HOST clicks NEW GAME and shares their IP address with friends.\n\n" +
                        "3. Other players click JOIN GAME and enter the Host's IP address.\n\n" +
                        "4. Once all players join, the game begins automatically.\n\n" +
                        "5. The blinking arrow shows whose turn it is — wait for yours.\n\n" +
                        "6. Click the dice on your turn to roll and move your token.\n\n" +
                        "7. Land on unowned property to buy it, or owned property to pay rent.\n\n" +
                        "8. Land on Chance for a random reward, Community Chest for $200.\n\n" +
                        "9. Land on Tax to pay $200, or Go To Jail to pay $100 bail.\n\n" +
                        "10. Pass GO to collect $200 salary and boost your rent multiplier.\n\n" +
                        "11. At end of turn, optionally sell any property for half its price.\n\n" +
                        "12. A player who runs out of money goes BANKRUPT and is eliminated.\n\n" +
                        "13. Last player standing wins!"
        );
        alert.showAndWait();
    }

    @FXML
    void MouseEnteredOnHelp(MouseEvent event) {
       player = new PlayNewMedia("/sound/hover_new.mp3");
       player.run();
    }



    @FXML
    void ClickedOnAbout(MouseEvent event) {
        new PlayNewMedia("/sound/click.mp3").run();
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("About Us");
        alert.setHeaderText("Network Monopoly");
        alert.setContentText(
                "Developed by LEARNERS\n\n" +
                        "Supervised by\n"+
                        "Lecturer Md. Nurul Muttakin, CSE, BUET\n\n"+
                        "Project Submitted By:\n"+
                        "Shafiee Al Ehtesham, ID: 2405121\n"+
                        "Rajdip Deb, ID: 2405122\n\n"+
                        "A JavaFX multiplayer Monopoly game\n" +
                        "supporting up to 8 players over LAN.\n\n" +
                        "Built with:\n" +
                        " - Java & JavaFX\n" +
                        " - JFoenix UI Library\n" +
                        " - Custom TCP Socket Networking\n\n" +
                        "Version: 1.0"
        );
        alert.showAndWait();
    }

    @FXML
    void MouseEnteredOnAbout(MouseEvent event) {
        player = new PlayNewMedia("/sound/hover_new.mp3");
        player.run();
    }



    @FXML
    void clicked_Exit(MouseEvent event) {
        new PlayNewMedia("/sound/click.mp3").run();
        player = new PlayNewMedia("/sound/quit exited.mp3");
        player.run();
        ((Stage) button_exit.getScene().getWindow()).close();
    }

    @FXML
    void MouseEnteredOnExit(MouseEvent event) {
        player = new PlayNewMedia("/sound/hover_new.mp3");
        player.run();
    }



    @FXML
    void ClickedOnnew(MouseEvent event) throws java.io.IOException {
        new PlayNewMedia("/sound/click.mp3").run();
        String text = textfield_amount.getText();
        if (text == null || text.trim().isEmpty()) {
            textfield_amount.setText("1500");
            Main.players_amount = 1500;
        } else {
            try {
                Main.players_amount = Integer.parseInt(text.trim());
            } catch (NumberFormatException e) {
                textfield_amount.setText("1500");
                Main.players_amount = 1500;
            }
        }

        Networking.MonopolyServer.startServer(application.Main.players_number, application.Main.players_amount);
        Networking.ClientNetworkManager.instance.connectToServer("localhost");

        String myIP = "localhost";
        try { myIP = java.net.InetAddress.getLocalHost().getHostAddress(); } catch (Exception e) {}

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Host Server Started");
        alert.setHeaderText("Waiting for " + application.Main.players_number + " players...");
        alert.setContentText("Give this IP Address to your friends:\n\n" + myIP);
        alert.showAndWait();

        Parent root = FXMLLoader.load(getClass().getResource("/FXML_files/SecondPage.fxml"));

        javafx.geometry.Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        double scaleFactor = Math.min(1.0, (bounds.getHeight() - 40) / 980.0);

        javafx.scene.transform.Scale scale = new javafx.scene.transform.Scale(scaleFactor, scaleFactor, 0, 0);
        root.getTransforms().add(scale);

        Stage stage = new Stage();
        Scene scene = new Scene(root, 1100 * scaleFactor, 980 * scaleFactor);
        stage.setTitle("Monopoly - Game Board");
        stage.setScene(scene);
        stage.show();
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);

        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(
                javafx.util.Duration.seconds(0.2), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        new PlayNewMedia("/sound/Popup_SecondPage.mp3").run();
        ((Stage) button_new.getScene().getWindow()).close();
    }

    @FXML
    void MouseEntred_New_Button(MouseEvent event) {
        player = new PlayNewMedia("/sound/hover_new.mp3");
        player.run();
    }



    @FXML
    void Clicked_On_Back(MouseEvent event) {
        new PlayNewMedia("/sound/click.mp3").run();
        new PlayNewMedia("/sound/hover_back.mp3").run();
        AnchorPane_start.setVisible(false);
        Center_AnchorPane.setVisible(true);
    }

    @FXML
    void MouseEntred_Back_Button(MouseEvent event) {
        player = new PlayNewMedia("/sound/hover_new.mp3");
        player.run();
    }



    @FXML void mouse_clickOn_1(MouseEvent event) { selectPlayers(1, 0); }
    @FXML void mouse_clickOn_2(MouseEvent event) { selectPlayers(2, 0); }
    @FXML void mouse_clickOn_3(MouseEvent event) { selectPlayers(3, 40); }
    @FXML void mouse_clickOn_4(MouseEvent event) { selectPlayers(4, 80); }
    @FXML void mouse_clickOn_5(MouseEvent event) { selectPlayers(5, 120); }
    @FXML void mouse_clickOn_6(MouseEvent event) { selectPlayers(6, 160); }
    @FXML void mouse_clickOn_7(MouseEvent event) { selectPlayers(7, 200); }
    @FXML void mouse_clickOn_8(MouseEvent event) { selectPlayers(8, 240); }

    private void selectPlayers(int number, double triangleX) {
        image_triangle.setX(triangleX);
        player = new PlayNewMedia("/sound/Select number of players.mp3");
        player.run();
        Main.players_number = number;
    }
}
