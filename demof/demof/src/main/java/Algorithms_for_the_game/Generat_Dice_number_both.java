package Algorithms_for_the_game;

import FXML_Controllers.SecondPage;
import Rules_Of_The_game.PlayerArea;
import Transitions.DominoTransitions;
import javafx.animation.PauseTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import application.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generat_Dice_number_both {

    private final ImageView diceVar1, diceVar2;
    private final int[] diceValues = new int[2];
    private PlayerArea currentPlayer;
    private final Random random = new Random();

    public Generat_Dice_number_both(ImageView dVar1, ImageView dVar2) {
        this.diceVar1 = dVar1;
        this.diceVar2 = dVar2;


    }

    public void Mov_player(int die1, int die2) {
        try {
            syncCurrentPlayer();

            diceValues[0] = die1;
            diceValues[1] = die2;
            Main.player_turn = SecondPage.current_player_index;

            int totalMove = diceValues[0] + diceValues[1];

            if (currentPlayer != null && currentPlayer.getDomino() != null) {
                DominoTransitions moveObj = new DominoTransitions(
                        currentPlayer.getDomino(),
                        currentPlayer.getPlayer_place(),
                        totalMove
                );
                moveObj.run();
            }

            int oldPlace = currentPlayer.getPlayer_place();
            int newPlace = oldPlace + totalMove;

            boolean passedGo = false;
            if (newPlace > 40) {
                newPlace -= 40;
                passedGo = true;
            }

            boolean isChest = (newPlace == 3 || newPlace == 18 || newPlace == 34);
            boolean isChance = (newPlace == 8 || newPlace == 23 || newPlace == 37);
            boolean isTax = (newPlace == 5 || newPlace == 39);
            boolean isGoToJail = (newPlace == 31);
            boolean isCorner = (newPlace == 1 || newPlace == 11 || newPlace == 21);

            boolean isProperty = (!isChest && !isChance && !isTax && !isGoToJail && !isCorner);

            int ownerID = getOwnerOfProperty(newPlace);
            int propPrice = getStaticPropertyPrice(newPlace);
            int propRent = calculateDynamicRent(newPlace, ownerID, totalMove);


            if (ownerID != -1) {
                PlayerArea propertyOwner = getStaticPlayerByIndex(ownerID);
                if (propertyOwner != null) {
                    System.out.println(propRent);
                    propRent = propRent * propertyOwner.getExp_cnt();
                    System.out.println(propertyOwner.getExp_cnt());
                    System.out.println(propRent);
                }
            }

            boolean isOwnedByOther = (isProperty && ownerID != -1 && ownerID != SecondPage.current_player_index);
            boolean isOwnedByMe = (isProperty && ownerID == SecondPage.current_player_index);
            boolean isInsufficient = (isProperty && currentPlayer.getPlayer_amount() < propPrice);

            int finalPlaceToSave = isGoToJail ? 11 : newPlace;
            currentPlayer.setPlayer_place(finalPlaceToSave);
            Main.static_player_place = finalPlaceToSave;

            if (isGoToJail) {

                PauseTransition jailDelay = new PauseTransition(Duration.millis(totalMove * 300 + 100));
                jailDelay.setOnFinished(ev -> {
                    if (currentPlayer != null && currentPlayer.getDomino() != null) {

                        currentPlayer.getDomino().setTranslateX(0);
                        currentPlayer.getDomino().setTranslateY(0);

                        DominoTransitions jailMove = new DominoTransitions(
                                currentPlayer.getDomino(),
                                1,
                                10
                        );
                        jailMove.run();
                    }
                });
                jailDelay.play();
            }

            PauseTransition delay = new PauseTransition(Duration.millis(totalMove * 300 + 500));

            boolean finalPassedGo = passedGo;
            boolean finalIsChest = isChest;
            boolean finalIsChance = isChance;
            boolean finalIsTax = isTax;
            boolean finalIsGoToJail = isGoToJail;
            boolean finalIsCorner = isCorner;
            boolean finalIsProperty = isProperty;

            int finalPropRent = propRent;
            delay.setOnFinished(e -> {
                if (SecondPage.current_player_index != Networking.ClientNetworkManager.myPlayerID) {
                    return;
                }

                try {
                    if (finalPassedGo && !finalIsGoToJail) {
                        sendMoneySyncPacket(200);
                        System.out.println("Passed GO! Collected $200 Salary.");

                        currentPlayer.setExp_cnt(currentPlayer.getExp_cnt() + 1);
                        System.out.println(currentPlayer.getExp_cnt());

                        Networking.ClientNetworkManager.instance.sendPacket("UPDATE_EXP", SecondPage.current_player_index + "," + currentPlayer.getExp_cnt());
                    }

                    if (finalIsGoToJail) {
                        if (currentPlayer != null && (currentPlayer.getPlayer_amount() - 100) < 0) {
                            openChanceWindow("BANKRUPT! You couldn't afford 100$ bail.");
                            sendMoneySyncPacket(-100);
                        } else {
                            sendMoneySyncPacket(-100);
                            openChanceWindow("Go To Jail! You paid 100$ bail.");
                        }
                    }
                    else if (finalIsChest) {
                        sendMoneySyncPacket(200);
                        openChanceWindow("Community Chest! Received 200$.");
                    }
                    else if (finalIsChance) {
                        int[] chanceRewards = {50, 100, 150, 200};
                        int reward = chanceRewards[random.nextInt(chanceRewards.length)];
                        sendMoneySyncPacket(reward);
                        openChanceWindow("Chance! You found " + reward + "$.");
                    }
                    else if (finalIsTax) {
                        if (currentPlayer != null && (currentPlayer.getPlayer_amount() - 200) < 0) {
                            openChanceWindow("BANKRUPT! You couldn't afford 200$ tax.");
                            sendMoneySyncPacket(-200);
                        } else {
                            sendMoneySyncPacket(-200);
                            openChanceWindow("Tax! You paid 200$.");
                        }
                    }
                    else if (finalIsCorner) {
                        openChanceWindow("Safe Zone! You rest.");
                    }
                    else if (finalIsProperty) {
                        if (isOwnedByOther) {
                            payRent(ownerID, finalPropRent);
                        }
                        else if (isOwnedByMe) {
                            openChanceWindow("Your Property! You rest safely.");
                        }
                        else if (isInsufficient) {
                            openChanceWindow("Insufficient Funds to buy! Turn ends.");
                        }
                        else {
                            openPopUp(propPrice);
                        }
                    }
                    else {
                        triggerSellPhaseOrEndTurn();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    triggerSellPhaseOrEndTurn();
                }
            });
            delay.play();

        } catch (Throwable t) {
            t.printStackTrace();
            if (SecondPage.current_player_index == Networking.ClientNetworkManager.myPlayerID) {
                triggerSellPhaseOrEndTurn();
            }
        }
    }

    private void openChanceWindow(String message) {
        javafx.application.Platform.runLater(() -> {
            try {
                if (Main.Static_PopUp_Label != null) {
                    Main.Static_PopUp_Label.setText(message);
                    new MediaClass.PlayNewMedia("/sound/Chance_audio.mp3").run();
                }

                FXML_Controllers.Chance_page.isLandedByMe = true;
                Networking.ClientNetworkManager.instance.sendPacket("SHOW_CHANCE", message);

                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/FXML_files/Chance_page.fxml"));
                javafx.scene.Parent root = loader.load();
                javafx.stage.Stage stage = new javafx.stage.Stage();
                stage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
                stage.setScene(new javafx.scene.Scene(root, javafx.scene.paint.Color.TRANSPARENT));
                stage.show();
            } catch (Throwable e) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Notice");
                alert.setHeaderText("Game Event");
                alert.setContentText(message);
                alert.showAndWait();

                triggerSellPhaseOrEndTurn();
            }
        });
    }

    private void payRent(int ownerID, int rentAmount) {
        javafx.application.Platform.runLater(() -> {
            try {
                if (currentPlayer != null && (currentPlayer.getPlayer_amount() - rentAmount) < 0) {
                    openChanceWindow("BANKRUPT! You couldn't afford " + rentAmount + "$ to Player " + (ownerID + 1));
                } else {
                    openChanceWindow("Rent! Paid " + rentAmount + "$ to Player " + (ownerID + 1));
                }

                sendMoneySyncPacket(-rentAmount);
                if (Networking.ClientNetworkManager.gameBoard != null) {
                    Networking.ClientNetworkManager.gameBoard.networkUpdateMoney(ownerID, rentAmount);
                }
                Networking.ClientNetworkManager.instance.sendPacket("UPDATE_MONEY", ownerID + "," + rentAmount);

            } catch (Throwable e) {
                e.printStackTrace();
                triggerSellPhaseOrEndTurn();
            }
        });
    }

    private void openPopUp(int dynamicPrice) {
        if (SecondPage.current_player_index != Networking.ClientNetworkManager.myPlayerID) return;

        javafx.application.Platform.runLater(() -> {
            try {
                if (currentPlayer == null) {
                    triggerSellPhaseOrEndTurn();
                    return;
                }

                Main.Building_price = dynamicPrice;

                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/FXML_files/PoPupPage.fxml"));
                new MediaClass.PlayNewMedia("/sound/Buy.mp3").run();
                javafx.scene.Parent root = loader.load();

                javafx.stage.Stage stage = new javafx.stage.Stage();
                stage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
                javafx.scene.Scene scene = new javafx.scene.Scene(root);
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                stage.setScene(scene);
                stage.show();

            } catch (Throwable e) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                alert.setTitle("Buy Property");
                alert.setHeaderText("Property " + Main.static_player_place);
                alert.setContentText("Buy this property for $" + dynamicPrice + "?");

                alert.showAndWait().ifPresent(response -> {
                    if (response == javafx.scene.control.ButtonType.OK) {
                        if (currentPlayer != null && currentPlayer.getPlayer_amount() >= dynamicPrice) {
                            sendMoneySyncPacket(-dynamicPrice);
                            if (Networking.ClientNetworkManager.gameBoard != null) {
                                Networking.ClientNetworkManager.gameBoard.networkBuyProperty(Main.static_player_place, Networking.ClientNetworkManager.myPlayerID);
                            }
                            Networking.ClientNetworkManager.instance.sendPacket("BUY_PROPERTY", Main.static_player_place + "," + Networking.ClientNetworkManager.myPlayerID);
                        }
                    }
                    triggerSellPhaseOrEndTurn();
                });
            }
        });
    }

    private void sendMoneySyncPacket(int amount) {
        boolean willBeBankrupt = false;

        if (currentPlayer != null && amount < 0) {
            if ((currentPlayer.getPlayer_amount() + amount) < 0) {
                willBeBankrupt = true;
            }
        }

        if (Networking.ClientNetworkManager.gameBoard != null) {
            Networking.ClientNetworkManager.gameBoard.networkUpdateMoney(SecondPage.current_player_index, amount);
        }
        Networking.ClientNetworkManager.instance.sendPacket("UPDATE_MONEY", SecondPage.current_player_index + "," + amount);

        if (willBeBankrupt) {
            System.out.println("Player " + (SecondPage.current_player_index + 1) + " is BANKRUPT!");
            new MediaClass.PlayNewMedia("/sound/Failed.mp3").run();
            currentPlayer.setPlayer_amount(-9999);

            boolean[] owned = currentPlayer.getPlayers_owned_places();
            if (owned != null) {
                for (int i = 0; i < owned.length; i++) {
                    if (owned[i]) {
                        owned[i] = false;
                        removeVisualToken(i);
                    }
                }
            }
            Networking.ClientNetworkManager.instance.sendPacket("BANKRUPT", String.valueOf(SecondPage.current_player_index));
            checkGlobalWinCondition();
        }
    }

    public static void checkGlobalWinCondition() {
        javafx.application.Platform.runLater(() -> {
            PlayerArea[] players = {Main.Gamer1, Main.Gamer2, Main.Gamer3, Main.Gamer4, Main.Gamer5, Main.Gamer6, Main.Gamer7, Main.Gamer8};
            int activeCount = 0;
            int winnerID = -1;

            for (int i = 0; i < application.Main.players_number; i++) {
                if (players[i] != null && players[i].getPlayer_amount() >= 0) {
                    activeCount++;
                    winnerID = i;
                }
            }

            if (activeCount == 1 && winnerID != -1) {
                int finalWinner = winnerID + 1;
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over!");
                alert.setHeaderText("WE HAVE A WINNER! 🏆");
                alert.setContentText("Player " + finalWinner + " is the last one standing and wins the game!");
                alert.showAndWait();

                try {
                    java.util.List<javafx.stage.Window> openWindows = new java.util.ArrayList<>(javafx.stage.Window.getWindows());
                    for (javafx.stage.Window window : openWindows) {
                        if (window instanceof javafx.stage.Stage) {
                            ((javafx.stage.Stage) window).close();
                        }
                    }

                    javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(Generat_Dice_number_both.class.getResource("/FXML_files/FirstPage.fxml"));
                    javafx.scene.Parent root = loader.load();
                    javafx.stage.Stage menuStage = new javafx.stage.Stage();
                    menuStage.setScene(new javafx.scene.Scene(root));
                    menuStage.setTitle("Monopoly - Main Menu");
                    menuStage.show();

                } catch (Exception ex) {
                    System.out.println("Could not load Main Menu FXML. Check the file path!");
                    ex.printStackTrace();
                }
            }
        });
    }

    private int getOwnerOfProperty(int placeIndex) {
        PlayerArea[] players = {Main.Gamer1, Main.Gamer2, Main.Gamer3, Main.Gamer4, Main.Gamer5, Main.Gamer6, Main.Gamer7, Main.Gamer8};
        for (int i = 0; i < application.Main.players_number; i++) {
            if (players[i] != null && players[i].getPlayers_owned_places()[placeIndex]) {
                return i;
            }
        }
        return -1;
    }

    private void syncCurrentPlayer() {
        currentPlayer = getStaticPlayerByIndex(SecondPage.current_player_index);
    }

    public static PlayerArea getStaticPlayerByIndex(int id) {
        switch(id) {
            case 0: return Main.Gamer1; case 1: return Main.Gamer2;
            case 2: return Main.Gamer3; case 3: return Main.Gamer4;
            case 4: return Main.Gamer5; case 5: return Main.Gamer6;
            case 6: return Main.Gamer7; case 7: return Main.Gamer8;
            default: return null;
        }
    }

    public static int getStaticPropertyPrice(int tile) {
        switch(tile) {
            case 2: case 4: return 60;
            case 6: return 200;
            case 7: case 9: return 100;
            case 10: return 120;
            case 12: case 14: return 140;
            case 13: return 150;
            case 15: return 160;
            case 16: return 200;
            case 17: case 19: return 180;
            case 20: return 200;
            case 22: case 24: return 220;
            case 25: return 240;
            case 26: return 200;
            case 27: case 28: return 260;
            case 29: return 150;
            case 30: return 280;
            case 32: case 33: return 300;
            case 35: return 320;
            case 36: return 200;
            case 38: return 350;
            case 40: return 400;
            default: return 200;
        }
    }

    private int getPropertyRent(int tile) {
        switch(tile) {
            case 2: return 2; case 4: return 4;
            case 6: case 16: case 26: case 36: return 50;
            case 7: case 9: return 6; case 10: return 8;
            case 12: case 14: return 10; case 13: case 29: return 20;
            case 15: return 12;
            case 17: case 19: return 14; case 20: return 16;
            case 22: case 24: return 18; case 25: return 20;
            case 27: case 28: return 22; case 30: return 24;
            case 32: case 33: return 26; case 35: return 28;
            case 38: return 35; case 40: return 50;
            default: return 25;
        }
    }

    private int calculateDynamicRent(int tile, int ownerID, int diceRoll) {
        if (ownerID == -1) return 0;

        if (tile == 13 || tile == 29) {
            int[] utilities = {13, 29};
            int ownedCount = countOwnedProperties(ownerID, utilities);
            if (ownedCount == 2) return diceRoll * 10;
            else return diceRoll * 4;
        }

        if (tile == 6 || tile == 16 || tile == 26 || tile == 36) {
            int[] railroads = {6, 16, 26, 36};
            int ownedCount = countOwnedProperties(ownerID, railroads);
            switch (ownedCount) {
                case 1: return 25;
                case 2: return 50;
                case 3: return 100;
                case 4: return 200;
                default: return 25;
            }
        }

        int baseRent = getPropertyRent(tile);
        if (ownsFullColorSet(tile, ownerID)) {
            return baseRent * 2;
        }

        return baseRent;
    }

    private boolean ownsFullColorSet(int tile, int ownerID) {
        int[] brown = {2, 4};
        int[] lightBlue = {7, 9, 10};
        int[] pink = {12, 14, 15};
        int[] orange = {17, 19, 20};
        int[] red = {22, 24, 25};
        int[] yellow = {27, 28, 30};
        int[] green = {32, 33, 35};
        int[] darkBlue = {38, 40};

        int[][] allSets = {brown, lightBlue, pink, orange, red, yellow, green, darkBlue};

        for (int[] colorSet : allSets) {
            for (int t : colorSet) {
                if (t == tile) {
                    return countOwnedProperties(ownerID, colorSet) == colorSet.length;
                }
            }
        }
        return false;
    }

    private int countOwnedProperties(int ownerID, int[] properties) {
        int count = 0;
        for (int t : properties) {
            if (getOwnerOfProperty(t) == ownerID) {
                count++;
            }
        }
        return count;
    }


    public static void removeVisualToken(int tile) {
        javafx.application.Platform.runLater(() -> {
            try {

                PlayerArea[] players = {Main.Gamer1, Main.Gamer2, Main.Gamer3, Main.Gamer4, Main.Gamer5, Main.Gamer6, Main.Gamer7, Main.Gamer8};
                javafx.scene.Scene mainBoardScene = null;

                for (PlayerArea p : players) {
                    if (p != null && p.getDomino() != null && p.getDomino().getScene() != null) {
                        mainBoardScene = p.getDomino().getScene();
                        break;
                    }
                }

                if (mainBoardScene != null) {
                    javafx.scene.Node token = mainBoardScene.lookup("#Owned_" + tile);
                    if (token != null) {
                        token.setVisible(false);
                    } else {
                        System.out.println("Warning: #Owned_" + tile + " not found on the main board!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void triggerSellPhaseOrEndTurn() {
        javafx.application.Platform.runLater(() -> {
            int myID = Networking.ClientNetworkManager.myPlayerID;
            if (SecondPage.current_player_index != myID) return;

            PlayerArea me = getStaticPlayerByIndex(myID);
            if (me == null || me.getPlayer_amount() < 0) {
                executeActualEndTurn();
                return;
            }

            boolean ownsProperty = false;
            List<Integer> ownedTiles = new ArrayList<>();
            boolean[] owned = me.getPlayers_owned_places();
            for (int i = 0; i < owned.length; i++) {
                if (owned[i]) {
                    ownsProperty = true;
                    ownedTiles.add(i);
                }
            }

            if (!ownsProperty) {
                executeActualEndTurn();
                return;
            }

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sell Property");
            alert.setHeaderText("End of Turn Phase");
            alert.setContentText("You own properties. Would you like to sell any to the Bank for half price before ending your turn?");

            javafx.scene.control.ButtonType btnYes = new javafx.scene.control.ButtonType("Yes, Sell");
            javafx.scene.control.ButtonType btnNo = new javafx.scene.control.ButtonType("No, End Turn");
            alert.getButtonTypes().setAll(btnYes, btnNo);

            alert.showAndWait().ifPresent(response -> {
                if (response == btnYes) {
                    showSellSelection(me, ownedTiles);
                } else {
                    executeActualEndTurn();
                }
            });
        });
    }

    private static void showSellSelection(PlayerArea me, List<Integer> ownedTiles) {
        List<String> choices = new ArrayList<>();
        for (int tile : ownedTiles) {
            int halfPrice = getStaticPropertyPrice(tile) / 2;
            choices.add("Property " + tile + " (Sell for $" + halfPrice + ")");
        }

        javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Select Property");
        dialog.setHeaderText("Sell a Property to the Bank");
        dialog.setContentText("Choose a property:");


        java.util.Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String choice = result.get();
            int tileToSell = -1;
            for (int tile : ownedTiles) {
                if (choice.contains("Property " + tile + " ")) {
                    tileToSell = tile;
                    break;
                }
            }

            if (tileToSell != -1) {
                int halfPrice = getStaticPropertyPrice(tileToSell) / 2;

                me.getPlayers_owned_places()[tileToSell] = false;
                removeVisualToken(tileToSell);

                if (Networking.ClientNetworkManager.gameBoard != null) {
                    Networking.ClientNetworkManager.gameBoard.networkUpdateMoney(SecondPage.current_player_index, halfPrice);
                }
                Networking.ClientNetworkManager.instance.sendPacket("UPDATE_MONEY", SecondPage.current_player_index + "," + halfPrice);

                Networking.ClientNetworkManager.instance.sendPacket("SELL_PROPERTY", tileToSell + "," + SecondPage.current_player_index);

                javafx.scene.control.Alert success = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                success.setTitle("Sold!");
                success.setHeaderText(null);
                success.setContentText("Property " + tileToSell + " sold for $" + halfPrice + "! It is now buyable again.");
                success.showAndWait();

                triggerSellPhaseOrEndTurn();
            } else {

                executeActualEndTurn();
            }
        } else {

            executeActualEndTurn();
        }
    }

    public static void networkSellProperty(int tileIndex, int ownerID) {
        PlayerArea player = getStaticPlayerByIndex(ownerID);
        if (player != null && player.getPlayers_owned_places() != null) {
            player.getPlayers_owned_places()[tileIndex] = false;
            removeVisualToken(tileIndex);
        }
    }

    public static void executeActualEndTurn() {
        try {
            Networking.ClientNetworkManager.instance.sendPacket("END_TURN", "");
            if (Networking.ClientNetworkManager.gameBoard != null) {
                Networking.ClientNetworkManager.gameBoard.advanceTurn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}