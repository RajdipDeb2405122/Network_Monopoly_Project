package FXML_Controllers;

import Algorithms_for_the_game.Generat_Dice_number_both;
import Transitions.ChangeDiceNumber;
import Transitions.DiceTransition;
import application.Main;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import Rules_Of_The_game.PlayerArea;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.*;

public class SecondPage extends Main implements Initializable {


    @FXML private ImageView Dice_var1, Dice_var2;
    @FXML private ImageView Dice1, Dice2, Dice3, Dice4, Dice5, Dice6;


    @FXML private ImageView Domino_A, Domino_B, Domino_C, Domino_D;
    @FXML private ImageView Domino_E, Domino_F, Domino_G, Domino_H;


    @FXML private ImageView Arrow_1, Arrow_2, Arrow_3, Arrow_4;
    @FXML private ImageView Arrow_5, Arrow_6, Arrow_7, Arrow_8;


    @FXML private Label Label_player1, Label_player2, Label_player3, Label_player4;
    @FXML private Label Label_player5, Label_player6, Label_player7, Label_player8;


    @FXML private Label Label_player1_Amount, Label_player2_Amount, Label_player3_Amount, Label_player4_Amount;
    @FXML private Label Label_player5_Amount, Label_player6_Amount, Label_player7_Amount, Label_player8_Amount;


    @FXML private ImageView Owned_1, Owned_2, Owned_3, Owned_4, Owned_5;
    @FXML private ImageView Owned_6, Owned_7, Owned_8, Owned_9, Owned_10;
    @FXML private ImageView Owned_11, Owned_12, Owned_13, Owned_14, Owned_15;
    @FXML private ImageView Owned_16, Owned_17, Owned_18, Owned_19, Owned_20;
    @FXML private ImageView Owned_21, Owned_22, Owned_23, Owned_24, Owned_25;
    @FXML private ImageView Owned_26, Owned_27, Owned_28, Owned_29, Owned_30;
    @FXML private ImageView Owned_31, Owned_32, Owned_33, Owned_34, Owned_35;
    @FXML private ImageView Owned_36, Owned_37, Owned_38, Owned_39, Owned_40;


    public List<Label> nameLabels;
    public List<Label> amountLabels;
    public List<ImageView> playerDoms;
    public List<ImageView> playerArrows;

    private int mouse_click_on_Dice = 0;
    public static int current_player_index = 0;
    public static boolean isCurrentlyRolling = false;


    @Override
    public void initialize(URL location, ResourceBundle arg1) {


        Networking.ClientNetworkManager.gameBoard = this;

        nameLabels = Arrays.asList(
                Label_player1, Label_player2, Label_player3, Label_player4,
                Label_player5, Label_player6, Label_player7, Label_player8
        );

        amountLabels = Arrays.asList(
                Label_player1_Amount, Label_player2_Amount, Label_player3_Amount, Label_player4_Amount,
                Label_player5_Amount, Label_player6_Amount, Label_player7_Amount, Label_player8_Amount
        );

        playerDoms = Arrays.asList(
                Domino_A, Domino_B, Domino_C, Domino_D,
                Domino_E, Domino_F, Domino_G, Domino_H
        );

        playerArrows = Arrays.asList(
                Arrow_1, Arrow_2, Arrow_3, Arrow_4,
                Arrow_5, Arrow_6, Arrow_7, Arrow_8
        );

        setupPlayers(Main.players_number);
        setupArrowAnimations();
        initializeOwnedBuildings();

        //"X"
        javafx.application.Platform.runLater(() -> {
            try {
                javafx.stage.Stage stage = (javafx.stage.Stage) Owned_32.getScene().getWindow();

                stage.setOnCloseRequest(event -> {
                    System.out.println("X button clicked! Disconnecting cleanly...");
                    if (Networking.ClientNetworkManager.instance != null) {
                        Networking.ClientNetworkManager.instance.sendPacket("BANKRUPT", String.valueOf(Networking.ClientNetworkManager.myPlayerID));
                        Networking.ClientNetworkManager.instance.disconnect();
                    }
                    javafx.application.Platform.exit();
                    System.exit(0);
                });
            } catch (Exception e) {
                System.out.println("Could not attach X button listener.");
            }
        });



    }

    private void initializeOwnedBuildings() {
        images_owned_buildings[1] = Owned_1; images_owned_buildings[2] = Owned_2;
        images_owned_buildings[3] = Owned_3; images_owned_buildings[4] = Owned_4;
        images_owned_buildings[5] = Owned_5; images_owned_buildings[6] = Owned_6;
        images_owned_buildings[7] = Owned_7; images_owned_buildings[8] = Owned_8;
        images_owned_buildings[9] = Owned_9; images_owned_buildings[10] = Owned_10;
        images_owned_buildings[11] = Owned_11; images_owned_buildings[12] = Owned_12;
        images_owned_buildings[13] = Owned_13; images_owned_buildings[14] = Owned_14;
        images_owned_buildings[15] = Owned_15; images_owned_buildings[16] = Owned_16;
        images_owned_buildings[17] = Owned_17; images_owned_buildings[18] = Owned_18;
        images_owned_buildings[19] = Owned_19; images_owned_buildings[20] = Owned_20;
        images_owned_buildings[21] = Owned_21; images_owned_buildings[22] = Owned_22;
        images_owned_buildings[23] = Owned_23; images_owned_buildings[24] = Owned_24;
        images_owned_buildings[25] = Owned_25; images_owned_buildings[26] = Owned_26;
        images_owned_buildings[27] = Owned_27; images_owned_buildings[28] = Owned_28;
        images_owned_buildings[29] = Owned_29; images_owned_buildings[30] = Owned_30;
        images_owned_buildings[31] = Owned_31; images_owned_buildings[32] = Owned_32;
        images_owned_buildings[33] = Owned_33; images_owned_buildings[34] = Owned_34;
        images_owned_buildings[35] = Owned_35; images_owned_buildings[36] = Owned_36;
        images_owned_buildings[37] = Owned_37; images_owned_buildings[38] = Owned_38;
        images_owned_buildings[39] = Owned_39; images_owned_buildings[40] = Owned_40;

        for (int i = 1; i <= 40; i++) {
            if (images_owned_buildings[i] != null) {
                images_owned_buildings[i].setVisible(false);
            }
        }
    }



    @FXML void Click_Dice_1(MouseEvent event) { rollDiceLogic(); }
    @FXML void Click_Dice_2(MouseEvent event) { rollDiceLogic(); }

    private void rollDiceLogic() {
        if (!Networking.ClientNetworkManager.isGameReady) {
            showDiagnosticWarning("Game Not Ready", "Waiting for players to join..."); return;
        }
        if (current_player_index != Networking.ClientNetworkManager.myPlayerID) {
            showDiagnosticWarning("Not Your Turn!", "It is Player " + (current_player_index + 1) + "'s turn.\nYou are Player " + (Networking.ClientNetworkManager.myPlayerID + 1));
            return;
        }
        if (isCurrentlyRolling) {
            showDiagnosticWarning("Please Wait", "The dice are currently locked."); return;
        }

        isCurrentlyRolling = true;
        int num1 = new java.util.Random().nextInt(6) + 1;
        int num2 = new java.util.Random().nextInt(6) + 1;

        Networking.ClientNetworkManager.instance.sendPacket("ROLL_DICE", num1 + "," + num2);
        networkRollDice(num1, num2);
    }

    private void showDiagnosticWarning(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public void networkRollDice(int d1, int d2) {
        mouse_click_on_Dice = (mouse_click_on_Dice >= 10) ? 1 : mouse_click_on_Dice + 1;

        try {
            new Transitions.DiceTransition(Dice_var1, Dice_var2, mouse_click_on_Dice).run();
            new Transitions.ChangeDiceNumber(Dice1, Dice2, Dice3, Dice4, Dice5, Dice6, Dice_var1, Dice_var2, d1, d2).run();
        } catch (Throwable e) {
            System.out.println("Dice animation skipped - missing images.");
        }

        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
        delay.setOnFinished(e -> {
            Algorithms_for_the_game.Generat_Dice_number_both logic = new Algorithms_for_the_game.Generat_Dice_number_both(Dice_var1, Dice_var2);
            logic.Mov_player(d1, d2);
        });
        delay.play();
    }


    public void advanceTurn() {
        javafx.application.Platform.runLater(() -> {
            try {
                if (playerArrows != null && current_player_index >= 0 && current_player_index < playerArrows.size()) {
                    playerArrows.get(current_player_index).setVisible(false);
                }
            } catch (Exception e) {}

            int attempts = 0;
            do {
                current_player_index = (current_player_index + 1) % application.Main.players_number;
                attempts++;
            } while (attempts <= application.Main.players_number &&
                    getPlayerByIndex(current_player_index) != null &&
                    getPlayerByIndex(current_player_index).getPlayer_amount() < 0);

            try {
                if (playerArrows != null && current_player_index >= 0 && current_player_index < playerArrows.size()) {
                    playerArrows.get(current_player_index).setVisible(true);
                }
            } catch (Exception e) {}

            isCurrentlyRolling = false;
        });
    }



    private Rules_Of_The_game.PlayerArea getPlayerByIndex(int index) {
        switch (index) {
            case 0: return application.Main.Gamer1;
            case 1: return application.Main.Gamer2;
            case 2: return application.Main.Gamer3;
            case 3: return application.Main.Gamer4;
            case 4: return application.Main.Gamer5;
            case 5: return application.Main.Gamer6;
            case 6: return application.Main.Gamer7;
            case 7: return application.Main.Gamer8;
            default: return null;
        }
    }

//    private boolean isBankrupt(int pID) {
//        Rules_Of_The_game.PlayerArea p = getPlayerByIndex(pID);
//        return p != null && p.getPlayer_amount() < 0;
//    }

    public void networkBankrupt(int pID) {
        javafx.application.Platform.runLater(() -> {
            Rules_Of_The_game.PlayerArea p = getPlayerByIndex(pID);
            if (p != null) {


                p.setPlayer_amount(-9999);

                if (p.getDomino() != null) p.getDomino().setVisible(false);

                if (nameLabels != null && nameLabels.get(pID) != null) {
                    nameLabels.get(pID).setText("BANKRUPT");
                }

                boolean[] owned = p.getPlayers_owned_places();
                if (owned != null) {
                    for (int i = 1; i <= 40; i++) {
                        if (owned[i]) {
                            owned[i] = false;
                            if (application.Main.images_owned_buildings[i] != null) {
                                application.Main.images_owned_buildings[i].setVisible(false);
                            }
                        }
                    }
                }
            }
        });
    }

    public void networkUpdateMoney(int pID, int amountChange) {
        javafx.application.Platform.runLater(() -> {
            Rules_Of_The_game.PlayerArea player = getPlayerByIndex(pID);
            if (player != null) {
                player.setPlayer_amount(player.getPlayer_amount() + amountChange);
                if (amountLabels != null && amountLabels.get(pID) != null) {
                    amountLabels.get(pID).setText(player.getPlayer_amount() + " $");
                }
                System.out.println("Global Sync: Player " + (pID + 1) + " money is now " + player.getPlayer_amount());
            }
        });
    }


//    public void networkShowChance(String message) {
//        javafx.application.Platform.runLater(() -> {
//            try {
//                if (application.Main.Static_PopUp_Label != null) {
//                    application.Main.Static_PopUp_Label.setText(message);
//                }
//                FXML_Controllers.Chance_page.isLandedByMe = false;
//
//                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/FXML_files/Chance_page.fxml"));
//                javafx.scene.Parent root = loader.load();
//
//                javafx.stage.Stage stage = new javafx.stage.Stage();
//                stage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
//                javafx.scene.Scene scene = new javafx.scene.Scene(root);
//                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
//                stage.setScene(scene);
//                stage.show();
//            } catch (Exception e) {
//                System.out.println("Opponent Screen FXML Failed! Using Backup Alert.");
//
//                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
//                alert.setTitle("Notice");
//                alert.setHeaderText("Opponent Event");
//                alert.setContentText(message);
//                alert.show();
//            }
//        });
//    }

    public void networkBuyProperty(int placeIndex, int buyerID) {
        javafx.application.Platform.runLater(() -> {
            Rules_Of_The_game.PlayerArea buyer = getPlayerByIndex(buyerID);
            if (buyer != null) {
                buyer.getPlayers_owned_places()[placeIndex] = true;

                if (Main.images_owned_buildings[placeIndex] != null) {
                    String[] playerIcons = {
                            "/images/Player_A.png", "/images/Player_B.png",
                            "/images/Player_C.png", "/images/Player_D.png",
                            "/images/Player_E.png", "/images/Player_F.png",
                            "/images/Player_G.png", "/images/Player_H.png"
                    };
                    try {
                        javafx.scene.image.Image ownerSymbol = new javafx.scene.image.Image(getClass().getResourceAsStream(playerIcons[buyerID]));
                        Main.images_owned_buildings[placeIndex].setImage(ownerSymbol);
                        Main.images_owned_buildings[placeIndex].setVisible(true);
                    } catch (Exception e) {}
                }
            }
        });
    }



    public void refreshPlayerUI() {
        setupPlayers(application.Main.players_number);
    }

    private void setupPlayers(int count) {
        application.Main.Gamer1 = new Rules_Of_The_game.PlayerArea("Player 1", application.Main.players_amount, Domino_A, Arrow_1, 1, Label_player1_Amount);
        application.Main.Gamer2 = new Rules_Of_The_game.PlayerArea("Player 2", application.Main.players_amount, Domino_B, Arrow_2, 1, Label_player2_Amount);
        application.Main.Gamer3 = new Rules_Of_The_game.PlayerArea("Player 3", application.Main.players_amount, Domino_C, Arrow_3, 1, Label_player3_Amount);
        application.Main.Gamer4 = new Rules_Of_The_game.PlayerArea("Player 4", application.Main.players_amount, Domino_D, Arrow_4, 1, Label_player4_Amount);
        application.Main.Gamer5 = new Rules_Of_The_game.PlayerArea("Player 5", application.Main.players_amount, Domino_E, Arrow_5, 1, Label_player5_Amount);
        application.Main.Gamer6 = new Rules_Of_The_game.PlayerArea("Player 6", application.Main.players_amount, Domino_F, Arrow_6, 1, Label_player6_Amount);
        application.Main.Gamer7 = new Rules_Of_The_game.PlayerArea("Player 7", application.Main.players_amount, Domino_G, Arrow_7, 1, Label_player7_Amount);
        application.Main.Gamer8 = new Rules_Of_The_game.PlayerArea("Player 8", application.Main.players_amount, Domino_H, Arrow_8, 1, Label_player8_Amount);

        String savedName = FXML_Controllers.Settings.playerName;
        int myID = Networking.ClientNetworkManager.myPlayerID;

        for (int i = 0; i < 8; i++) {
            boolean isActive = (i < count);
            nameLabels.get(i).setVisible(isActive);
            amountLabels.get(i).setVisible(isActive);
            playerDoms.get(i).setVisible(isActive);
            playerArrows.get(i).setVisible(isActive && i == current_player_index);

            if (!isActive) {
                playerDoms.get(i).setOpacity(0);
            } else {
                playerDoms.get(i).setOpacity(1);

                if (i == myID && savedName != null && !savedName.trim().isEmpty()) {
                    nameLabels.get(i).setText(savedName.trim());
                } else {
                    nameLabels.get(i).setText("Player " + (i + 1));
                }

                amountLabels.get(i).setText(application.Main.players_amount + " $");
            }
        }
    }

    private void setupArrowAnimations() {
        for (ImageView arrow : playerArrows) {
            FadeTransition ft = new FadeTransition(Duration.millis(500), arrow);
            ft.setFromValue(1.0);
            ft.setToValue(0.3);
            ft.setCycleCount(FadeTransition.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
        }
    }



    @FXML void Click_On_Back(MouseEvent event) {

        Networking.ClientNetworkManager.instance.sendPacket("BANKRUPT", String.valueOf(Networking.ClientNetworkManager.myPlayerID));
        Networking.ClientNetworkManager.instance.disconnect();

        current_player_index = 0;
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/FXML_files/FirstPage.fxml"));
            Stage firstPageStage = new Stage();

            javafx.geometry.Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            double scaleFactor = Math.min(1.0, bounds.getHeight() / 700.0);
            javafx.scene.transform.Scale scale = new javafx.scene.transform.Scale(scaleFactor, scaleFactor, 0, 0);
            root.getTransforms().add(scale);

            javafx.scene.Scene scene = new javafx.scene.Scene(root, 600 * scaleFactor, 700 * scaleFactor);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            firstPageStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
            firstPageStage.setTitle("Monopoly - Main Menu");
            firstPageStage.setScene(scene);

            firstPageStage.show();
            firstPageStage.setX((bounds.getWidth() - firstPageStage.getWidth()) / 2);
            firstPageStage.setY((bounds.getHeight() - firstPageStage.getHeight()) / 2);

            Stage currentStage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML void Click_On_Quit(MouseEvent event) {
        System.out.println("Quitting game...");
        Networking.ClientNetworkManager.instance.sendPacket("BANKRUPT", String.valueOf(Networking.ClientNetworkManager.myPlayerID));
        Networking.ClientNetworkManager.instance.disconnect();

        javafx.application.Platform.exit();
        System.exit(0);
    }



    @FXML void Click_On_Arena(MouseEvent event) {}
    @FXML void Click_On_image_under_players_info(MouseEvent event) {}
    @FXML void Butoon_click(ActionEvent event) {}
    @FXML void Click_On_Player1(MouseEvent event) { selectPlayer(0); }
    @FXML void Click_On_Player2(MouseEvent event) { selectPlayer(1); }
    @FXML void Click_On_Player3(MouseEvent event) { selectPlayer(2); }
    @FXML void Click_On_Player4(MouseEvent event) { selectPlayer(3); }
    @FXML void Click_On_Player5(MouseEvent event) { selectPlayer(4); }
    @FXML void Click_On_Player6(MouseEvent event) { selectPlayer(5); }
    @FXML void Click_On_Player7(MouseEvent event) { selectPlayer(6); }
    @FXML void Click_On_Player8(MouseEvent event) { selectPlayer(7); }

    private void selectPlayer(int index) {
        current_player_index = index;
        for (int i = 0; i < playerArrows.size(); i++) {
            playerArrows.get(i).setVisible(i == index);
        }
    }

    @FXML void Click_ON_Symbole_Player1(MouseEvent event) { }
    @FXML void Click_ON_Symbole_Player2(MouseEvent event) { }
    @FXML void Click_ON_Symbole_Player3(MouseEvent event) { }
    @FXML void Click_ON_Symbole_Player4(MouseEvent event) { }
    @FXML void Click_ON_Symbole_Player5(MouseEvent event) { }
    @FXML void Click_ON_Symbole_Player6(MouseEvent event) { }
    @FXML void Click_ON_Symbole_Player7(MouseEvent event) { }
    @FXML void Click_ON_Symbole_Player8(MouseEvent event) { }
}