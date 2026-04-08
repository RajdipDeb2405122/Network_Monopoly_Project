package FXML_Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import Rules_Of_The_game.PlayerArea;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PoPupPage extends Main implements Initializable {

    @FXML private JFXButton Button_Yes, Button_No;
    @FXML private Label PopUp_Label;
    private boolean actionTaken = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (PopUp_Label != null) {
            PopUp_Label.setText("Buy property " + Main.static_player_place + " for 200$?");
        }
    }

//    @FXML
//    void Click_ON_Yes(MouseEvent event) {
//        if (actionTaken) return;
//        actionTaken = true;
//
//        int turn = SecondPage.current_player_index;
//        PlayerArea currentPlayer = null;
//        switch (turn) {
//            case 0: currentPlayer = Gamer1; break;
//            case 1: currentPlayer = Gamer2; break;
//            case 2: currentPlayer = Gamer3; break;
//            case 3: currentPlayer = Gamer4; break;
//            case 4: currentPlayer = Gamer5; break;
//            case 5: currentPlayer = Gamer6; break;
//            case 6: currentPlayer = Gamer7; break;
//            case 7: currentPlayer = Gamer8; break;
//        }
//
//        if (currentPlayer != null && currentPlayer.getPlayer_amount() >= 200) {
//            // 1. DECOUPLED MONEY: Send -200 through the global money pipeline
//            if (Networking.ClientNetworkManager.gameBoard != null) {
//                Networking.ClientNetworkManager.gameBoard.networkUpdateMoney(turn, -200);
//            }
//            Networking.ClientNetworkManager.instance.sendPacket("UPDATE_MONEY", turn + ",-200");
//
//            // 2. VISUAL CLAIM: Only update the flags on the board
//            if (Networking.ClientNetworkManager.gameBoard != null) {
//                Networking.ClientNetworkManager.gameBoard.networkBuyProperty(Main.static_player_place, turn);
//            }
//            Networking.ClientNetworkManager.instance.sendPacket("BUY_PROPERTY", Main.static_player_place + "," + turn);
//
//            try {
//                new MediaClass.PlayNewMedia("/sound/Yes Buy.mp3").run();
//            } catch (Exception e) {}
//        }
//        closeWindow(event);
//    }
//
//    @FXML
//    void Click_ON_NO(MouseEvent event) {
//        if (actionTaken) return;
//        actionTaken = true;
//        closeWindow(event);
//    }

    private void closeWindow(MouseEvent event) {
        Networking.ClientNetworkManager.instance.sendPacket("END_TURN", "");
        if (Networking.ClientNetworkManager.gameBoard != null) {
            Networking.ClientNetworkManager.gameBoard.advanceTurn();
        }
        Stage stage = (Stage) Button_Yes.getScene().getWindow();
        stage.close();
    }
}