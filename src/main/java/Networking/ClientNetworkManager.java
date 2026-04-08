package Networking;

import FXML_Controllers.SecondPage;
import javafx.application.Platform;
import java.io.*;
import java.net.*;

public class ClientNetworkManager {
    public static ClientNetworkManager instance = new ClientNetworkManager();
    public static SecondPage gameBoard;
    public static int myPlayerID = 0;
    public static boolean isGameReady = false;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public boolean intentionalDisconnect = false;

    public void disconnect() {
        intentionalDisconnect = true;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception e) {}
    }

    public void connectToServer(String ipAddress) {
        intentionalDisconnect = false; //Reset joining a new game

        new Thread(() -> {
            try {
                socket = new Socket(ipAddress, 8080);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                System.out.println("Successfully connected to the server!");

                while (true) {
                    GamePacket packet = (GamePacket) in.readObject();
                    try {
                        handleIncomingPacket(packet);
                    } catch (Exception ex) {
                        System.out.println("Safely ignored a packet processing error.");
                    }
                }
            } catch (Exception e) {


                if (intentionalDisconnect) {
                    System.out.println("Cleanly disconnected from game.");
                    return;
                }



                javafx.application.Platform.runLater(() -> {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setTitle("Connection Lost");
                    alert.setHeaderText("Game Disconnected");
                    alert.setContentText("The host left the game, or the server went offline!");
                    alert.showAndWait();

                    try {


                        java.util.List<javafx.stage.Window> openWindows = new java.util.ArrayList<>(javafx.stage.Window.getWindows());
                        for (javafx.stage.Window window : openWindows) {
                            if (window instanceof javafx.stage.Stage) {
                                ((javafx.stage.Stage) window).close();
                            }
                        }

                        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/FXML_files/FirstPage.fxml"));
                        javafx.stage.Stage menuStage = new javafx.stage.Stage();
                        menuStage.setScene(new javafx.scene.Scene(loader.load()));
                        menuStage.setTitle("Monopoly - Main Menu");
                        menuStage.show();
                    } catch (Exception ex) {
                        System.exit(0);
                    }
                });
            }
        }).start();
    }

    public void sendPacket(String action, String details) {
        try {
            if (out != null) {
                out.writeObject(new GamePacket(action, details));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleIncomingPacket(GamePacket packet) {
        Platform.runLater(() -> {
            System.out.println("Network Message Received: " + packet.action);



            if (packet.action.equals("SYNC_SETTINGS")) {
                String[] parts = packet.details.split(",");
                application.Main.players_number = Integer.parseInt(parts[0]);
                application.Main.players_amount = Integer.parseInt(parts[1]);


                if (gameBoard != null) {
                    javafx.application.Platform.runLater(() -> {
                        gameBoard.refreshPlayerUI();
                    });
                }
            }

            else if (packet.action.equals("ASSIGN_ID")) {
                myPlayerID = Integer.parseInt(packet.details);
                System.out.println("Assigned Player ID: " + myPlayerID);
            }
            else if (packet.action.equals("GAME_READY")) {
                isGameReady = true;
                System.out.println("Lobby is full! Game is Ready!");


                javafx.application.Platform.runLater(() -> {
                    try {
                        String myName = FXML_Controllers.Settings.playerName;
                        if (myName == null || myName.trim().isEmpty()) {
                            myName = "Player " + (myPlayerID + 1);
                        }


                        ClientNetworkManager.instance.sendPacket("UPDATE_NAME", myPlayerID + "," + myName);


                        if (gameBoard != null && gameBoard.nameLabels != null && myPlayerID < gameBoard.nameLabels.size()) {
                            gameBoard.nameLabels.get(myPlayerID).setText(myName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            else if (packet.action.equals("ROLL_DICE")) {
                if (gameBoard != null) {
                    String[] numbers = packet.details.split(",");
                    int d1 = Integer.parseInt(numbers[0]);
                    int d2 = Integer.parseInt(numbers[1]);
                    gameBoard.networkRollDice(d1, d2);
                }
            }

            else if (packet.action.equals("END_TURN")) {
                if (gameBoard != null) {
                    gameBoard.advanceTurn();
                }
            }

            else if (packet.action.equals("BUY_PROPERTY")) {
                if (gameBoard != null) {
                    String[] parts = packet.details.split(",");
                    int placeIndex = Integer.parseInt(parts[0]);
                    int buyerID = Integer.parseInt(parts[1]);
                    gameBoard.networkBuyProperty(placeIndex, buyerID);
                }
            }


            else if (packet.action.equals("UPDATE_MONEY")) {
                if (gameBoard != null) {
                    String[] parts = packet.details.split(",");
                    int pID = Integer.parseInt(parts[0]);
                    int amt = Integer.parseInt(parts[1]);
                    gameBoard.networkUpdateMoney(pID, amt);
                }
            }

            else if (packet.action.equals("SHOW_CHANCE")) {
                if (application.Main.Static_PopUp_Label != null) {
                    application.Main.Static_PopUp_Label.setText(packet.details);
                }
            }

            else if (packet.action.equals("BANKRUPT")) {
                Algorithms_for_the_game.Generat_Dice_number_both.checkGlobalWinCondition();
                if (gameBoard != null) {
                    int pID = Integer.parseInt(packet.details);
                    gameBoard.networkBankrupt(pID);
                }
            }

            else if (packet.action.equals("UPDATE_EXP")) {
                String[] parts = packet.details.split(",");
                int pID = Integer.parseInt(parts[0]);
                int newExp = Integer.parseInt(parts[1]);

                Rules_Of_The_game.PlayerArea p = Algorithms_for_the_game.Generat_Dice_number_both.getStaticPlayerByIndex(pID);
                if (p != null) {
                    p.setExp_cnt(newExp);
                    System.out.println("Player " + (pID + 1) + "'s Exp Multiplier synced to: " + newExp);
                }
            }


            else if (packet.action.equals("UPDATE_NAME")) {
                String[] parts = packet.details.split(",");
                int pID = Integer.parseInt(parts[0]);
                String newName = parts[1];

                if (gameBoard != null && gameBoard.nameLabels != null) {
                    javafx.application.Platform.runLater(() -> {

                        if (pID >= 0 && pID < gameBoard.nameLabels.size() && gameBoard.nameLabels.get(pID) != null) {
                            gameBoard.nameLabels.get(pID).setText(newName);
                            System.out.println("Player " + (pID + 1) + " changed name to: " + newName);
                        }
                    });
                }
            }

            else if (packet.action.equals("SELL_PROPERTY")) {

                String[] sellData = packet.details.split(",");
                int tileToSell = Integer.parseInt(sellData[0]);
                int previousOwner = Integer.parseInt(sellData[1]);


                Algorithms_for_the_game.Generat_Dice_number_both.networkSellProperty(tileToSell, previousOwner);
            }

            else if (packet.action.equals("REJECTED")) {
                javafx.application.Platform.runLater(() -> {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setTitle("Connection Error");
                    alert.setHeaderText("Lobby is Full!");
                    alert.setContentText("You cannot join because the game already has the maximum number of players.");
                    alert.showAndWait();

                    try {

                        java.util.List<javafx.stage.Window> openWindows = new java.util.ArrayList<>(javafx.stage.Window.getWindows());
                        for (javafx.stage.Window window : openWindows) {
                            if (window instanceof javafx.stage.Stage) {
                                ((javafx.stage.Stage) window).close();
                            }
                        }


                        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/FXML_files/FirstPage.fxml"));
                        javafx.stage.Stage menuStage = new javafx.stage.Stage();
                        menuStage.setScene(new javafx.scene.Scene(loader.load()));
                        menuStage.setTitle("Monopoly - Main Menu");
                        menuStage.show();
                    } catch (Exception e) {
                        System.exit(0);
                    }
                });
            }
        });
    }
}