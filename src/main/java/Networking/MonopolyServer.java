package Networking;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MonopolyServer {



    public static List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    static ServerSocket serverSocket;
    static boolean isRunning = false;

    public static int expectedPlayers = 8;
    public static int startingAmount = 1500;

    public static void startServer(int numPlayers, int amount) {
        if (isRunning) return;
        isRunning = true;
        clients.clear();
        expectedPlayers = numPlayers;
        startingAmount = amount;

        new Thread(() -> {
            System.out.println("Starting Server. Waiting for " + expectedPlayers + " players...");
            try {
                serverSocket = new ServerSocket(8080);
                while (isRunning) {
                    Socket socket = serverSocket.accept();

                    int assignedID = clients.size();
                    ClientHandler handler = new ClientHandler(socket, assignedID);
                    clients.add(handler);
                    new Thread(handler).start();
                }
            } catch (IOException e) {
                System.out.println("Server stopped.");
            }
        }).start();
    }

    public static void broadcast(GamePacket packet, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendPacket(packet);
            }
        }
    }

    public static void broadcastToAll(GamePacket packet) {
        for (ClientHandler client : clients) {
            client.sendPacket(packet);
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private int assignedID;

        public ClientHandler(Socket socket, int id) {
            this.socket = socket;
            this.assignedID = id;
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {


                if (this.assignedID >= MonopolyServer.expectedPlayers) {
                    System.out.println("Game is full. Rejecting extra player.");
                    sendPacket(new GamePacket("REJECTED", ""));
                    Thread.sleep(500);
                    socket.close();
                    MonopolyServer.clients.remove(this);
                    return;
                }

                System.out.println("Player " + (assignedID + 1) + " joined!");



                sendPacket(new GamePacket("ASSIGN_ID", String.valueOf(assignedID)));
                sendPacket(new GamePacket("SYNC_SETTINGS", MonopolyServer.expectedPlayers + "," + MonopolyServer.startingAmount));



                if (MonopolyServer.clients.size() == MonopolyServer.expectedPlayers) {
                    System.out.println("Lobby is full! Broadcasting GAME_READY to all players...");
                    MonopolyServer.broadcastToAll(new GamePacket("GAME_READY", ""));

                    try {
                        MonopolyServer.isRunning = false;
                        if (MonopolyServer.serverSocket != null && !MonopolyServer.serverSocket.isClosed()) {
                            MonopolyServer.serverSocket.close();
                        }
                    } catch (IOException e) {
                        System.out.println("Server socket closed successfully.");
                    }
                }



                while (true) {
                    GamePacket packet = (GamePacket) in.readObject();
                    MonopolyServer.broadcast(packet, this);
                }
            } catch (Exception e) {


                System.out.println("Player " + (assignedID + 1) + " disconnected!");
                MonopolyServer.clients.remove(this);
                MonopolyServer.broadcastToAll(new GamePacket("BANKRUPT", String.valueOf(assignedID)));
            }
        }

        public void sendPacket(GamePacket packet) {
            try {
                if (out != null) {
                    out.writeObject(packet);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}