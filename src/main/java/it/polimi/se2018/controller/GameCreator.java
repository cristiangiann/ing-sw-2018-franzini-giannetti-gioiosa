package it.polimi.se2018.controller;


import it.polimi.se2018.model.PlayerMessage;
import it.polimi.se2018.model.PlayerMessageTypeEnum;
import it.polimi.se2018.model.player.User;
import it.polimi.se2018.view.RemoteView;

import java.util.List;

public class GameCreator {
    private GameManager gameManager;
    private GameStarter gameStarter;
    private boolean gameStatus;
    private RemoteView remoteView;



    public GameCreator (List<User> userList, RemoteView remoteView){
        this.remoteView = remoteView;
        gameStatus = false;
        this.gameManager = null;
        this.gameStarter = new GameStarter(userList);

    }


    public void receiveFromClient(PlayerMessage playerMessage) {

        if(!gameStatus){

            if(playerMessage.getId().equals(PlayerMessageTypeEnum.CHOICE)){
                gameStatus = gameStarter.newChoice(playerMessage.getPlayerChoice());
            }
        }else if(gameManager==null){
            gameManager = new GameManager(remoteView, gameStarter.getGameBoard());
        }
        if(playerMessage.getId().equals(PlayerMessageTypeEnum.MOVE)){
            //chiamata a metodo gameManager non ancora definito
        }



    }
}
