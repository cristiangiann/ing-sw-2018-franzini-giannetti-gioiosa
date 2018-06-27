package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.player.Player;
import it.polimi.se2018.model.restriction.*;

import java.util.List;

/**
 * Manages the single player's move
 * @author Silvia Franzini
 */
public class PickController implements Action{
    private GameBoard gameBoard;
    private PlayerMove playerMove;

    private List<Restriction> restrictionList;

    /**
     * Builder method of PickController class
     * @param gameBoard the game board of the match with all the elements on the board
     */
    public PickController(GameBoard gameBoard){
        this.gameBoard = gameBoard;
        restrictionList = RestrictionManager.getStandardRestriction();
    }

    /**
     * Choice of the player
     * @return playerMove related to the action
     */
    public PlayerMove getPlayerMove() {
        return playerMove;
    }

    /**
     * Method used to call the correct action chosen by the player
     * @param playerMove choices of the player
     * @return true if action completed, false otherwise
     */
    public int doAction(GameBoard gameBoard0, PlayerMove playerMove, List<Player> roundPlayerOrder, Turn turn){
        //this.gameBoard = gameBoard;
        Die dieExtracted = null;
        this.playerMove = playerMove;
        switch (playerMove.getTypeOfChoice()){
            case PICK:
                try{
                    for(Player player: gameBoard.getPlayerList()) {
                        if (player.getNickname().equals(playerMove.getPlayer().getNickname())){
                            dieExtracted = gameBoard.getBoardDice().takeDie(playerMove.getDiceBoardIndex());
                            int error = placeDice(player, dieExtracted, playerMove.getDiceSchemaWhereToLeave().get(0));
                            if(error != 0){
                                gameBoard.getBoardDice().insertDie(dieExtracted);
                                return error;
                            }
                            //TODO: else Notifica VIEW
                        }
                    }
                }catch(IllegalArgumentException e){
                    if (dieExtracted != null) gameBoard.getBoardDice().insertDie(dieExtracted);

                    return 1000;
                }
                break;
            case TOOL:
                try{
                    useTool(playerMove);
                }catch(IllegalArgumentException e){
                    return 1000;
                }
                break;
            case PASS:
                break;
            case ROLL:
                for(Die die : gameBoard.getBoardDice().getDieList()){
                    try{
                        die.firstRoll();
                    }catch(RuntimeException e){
                        return 1000;
                    }
                }
                break;
            case EXTRACT:
                for(int i = 0; i < gameBoard.getPlayerList().size()*2 +1; i++){
                    //try{}catch da implementare nella classe BoardDice
                    Die die;
                    try{
                        die = gameBoard.getBagDice().extractDice();
                    }catch(RuntimeException e){
                        return 1000;
                    }
                    gameBoard.getBoardDice().insertDie(die);
                } break;
            default:
                break;
        }
        return 0;
    }

    /**
     * Method to place a die on the board
     * @param actualPlayer player that selected the action
     * @param die die chosen
     * @param position position where to put the die
     * @return true if action completed, false otherwise
     */
    private int placeDice(Player actualPlayer, Die die, Position position){

        for(Restriction restriction: restrictionList){
            int error = restriction.checkRestriction(actualPlayer.getSchemaCard(), die, playerMove.getDiceSchemaWhereToLeave().get(0));
            if(error != 0) return error;
        }

        actualPlayer.getSchemaCard().setDiceIntoCell(position,die);
        return 0;
    }

    /**
     * Method invokes the Tool card chosen by the player
     * @param playerMove the player choice
     * @return true if action completed, false otherwise
     */
    private boolean useTool(PlayerMove playerMove){
        return false;
    }

    @Override
    public int doDefaultMove(){
        return 0;
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}