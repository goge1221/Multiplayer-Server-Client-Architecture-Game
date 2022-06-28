package client.main.Player;

import MessagesBase.MessagesFromClient.PlayerRegistration;

import java.awt.*;

public class Player {
    private String playerId;
    private String studentFirstName;
    private String studentLastName;
    private String uaccount;
    private Point position;
    private boolean hasTreasure;

    Player(String studentFirstName, String studentLastName, String uaccount){
        playerId = "";
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.uaccount = uaccount;
        this.position = new Point();
        this.hasTreasure = false;
    }

    public String getPlayerId(){
        return playerId;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public String getUaccount() {
        return uaccount;
    }

    public void setPlayerId(String playerId){
        this.playerId = playerId;
    }

    public Point getPosition() {
        return position;
    }

    public boolean playerHasTreasure() {
        return hasTreasure;
    }

    public void pickTreasureUp(){
        hasTreasure = true;
    }
    public void setPosition(int x, int y){
        position = new Point(x,y);
    }
}
