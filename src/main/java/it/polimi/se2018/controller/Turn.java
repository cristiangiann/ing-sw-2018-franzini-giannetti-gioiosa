package it.polimi.se2018.controller;

import it.polimi.se2018.model.GameBoard;
import it.polimi.se2018.model.PlayerMove;
import it.polimi.se2018.model.TypeOfChoiceEnum;

import java.util.ArrayList;
import java.util.List;


/**
 * Controller's Class Turn
 * componing the round with all the action of the current player
 * @author Davide Gioiosa
 */

public class Turn {
    /**
     * table containing all the objects belonging to the match
     */
    private GameBoard gameBoard;
    /**
     * list of Actions of the player componing the turn
     */
    private List<Action> turnsActionsList;
    /**
     * informs if the action PICK has already done or not
     * it can be done once a Turn for each player
     */
    private boolean isPick;
    /**
     * informs if the action TOOL has already done or not
     * it can be done once a Turn for each player
     */
    private boolean isToolCardUsed;

    /**
     * Builder of Turn which composes the Round
     * @param gameBoard full table of the game
     */
    public Turn (GameBoard gameBoard){
        if (gameBoard == null){
            throw new NullPointerException("Insertion of null parameter gameBoard");
        }
        this.gameBoard = gameBoard;
        this.isPick = false;
        this.isToolCardUsed = false;
        startTurn();
    }

    /**
     * Creates the list of Action belonging the player for each Turn
     */
    private void startTurn() {
        turnsActionsList = new ArrayList<Action>();
    }

    /**
     * Start run creating the action and adding to the list of Action if it can be done
     * Check if the action PICK has already done or not, it can be done once for Turn
     * @return boolean to communicate the result of the action
     */
    public boolean runTurn (PlayerMove playerMove){
        if(playerMove == null){
            throw new RuntimeException("Insertion of a PlayerMove null");
        }
        if(playerMove.getTypeOfChoice().equals(TypeOfChoiceEnum.PICK) && isPick){
            return false;
        }
        if(playerMove.getTypeOfChoice().equals(TypeOfChoiceEnum.TOOL) && isToolCardUsed){
            return false;
        }

        Action action = new Action (gameBoard);

        if(action.selectAction(playerMove)){
            turnsActionsList.add(action);
            if(playerMove.getTypeOfChoice().equals(TypeOfChoiceEnum.PICK)){
                this.isPick = true;
            }
            else if(playerMove.getTypeOfChoice().equals(TypeOfChoiceEnum.TOOL)){
                this.isToolCardUsed = true;
            }
            return true;
        }
        return false;
    }

    /**
     * Communicate if the action received is the last of the player and ends the turn
     * @return boolean to communicate the end of the Turn for the current player
     */
    public boolean endTurn (){
        if(turnsActionsList.get(turnsActionsList.size()-1).getPlayerMove().getTypeOfChoice().equals(TypeOfChoiceEnum.PASS)){
            return true;
        }
        return false;
    }
}
