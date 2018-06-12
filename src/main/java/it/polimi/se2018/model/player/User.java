package it.polimi.se2018.model.player;

import java.io.Serializable;
import java.util.Random;

/**
 * Public Class User
 * @author Davide Gioiosa
 */

public class User implements Serializable {
    private String nickname;
    private Player player;
    private boolean connection;
    private TypeOfConnection typeOfConnection;

    /**
     * Builder: create a user with the id and the connection
     * @param typeOfConnection Type of connection chosen by user: RMI or Socket
     */
    public User (TypeOfConnection typeOfConnection){
        this.connection = true;
        this.typeOfConnection = typeOfConnection;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Sets the player in the game related to the User
     * @param player in the game, related to the user
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * @return nickname of the player that is related to the user
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return player related to the user
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Informs if the player is online or not
     * @return the state of the connection
     */
    public boolean isConnected() {
        return connection;
    }


    public String getUniqueCode(){

        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }

       return stringBuilder.toString();
    }

    public TypeOfConnection getTypeOfConnection() {
        return typeOfConnection;
    }
}
