package Networking;

import java.io.Serializable;



public class GamePacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public String action;
    public String details; //"Player1:5:4"

    public GamePacket(String action, String details) {
        this.action = action;
        this.details = details;
    }
}