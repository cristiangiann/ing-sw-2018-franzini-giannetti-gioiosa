package it.polimi.se2018.view;

import it.polimi.se2018.controller.OperationString;
import it.polimi.se2018.model.*;
import it.polimi.se2018.model.cards.ToolCard;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.view.graphic.InputFormatEnum;
import it.polimi.se2018.view.graphic.TypeOfInputAsked;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2018.model.Config.*;
import static it.polimi.se2018.model.CommandTypeEnum.*;
import static it.polimi.se2018.view.graphic.cli.CommandLinePrint.*;

/**
 * Controls the Syntax of message received and sets PlayerMove's attributes
 * @author Cristian Giannetti
 */
public class SyntaxController extends Observable<PlayerMessage> {

    /**
     * Game Board used for the checks
     */
    private ClientBoard clientBoard;
    /**
     * Player Move representing the action that the player wants to make
     */
    private PlayerMove playerMove;

    private ToolCard toolCard;

    private CommandTypeEnum commandTypeEnum;

    private InputFormatEnum inputFormatEnum;

    private PlayerMove receivedPlayerMove;

    private List<List<CommandTypeEnum>> commandTypeEnumLists;

    /**
     * Constructor sets the first nextCommandType
     */
    public SyntaxController(){
        inputFormatEnum = null;
        commandTypeEnum = TYPEOFCHOICE;
    }

    public TypeOfInputAsked newMoveReceived(PlayerMove receivedPlayerMove, ClientBoard clientBoard){
        this.clientBoard = clientBoard;
        this.receivedPlayerMove = receivedPlayerMove.getClone();
        this.playerMove = receivedPlayerMove;
        setNextCommandType(receivedPlayerMove);
        return new TypeOfInputAsked(commandTypeEnum, inputFormatEnum,0, nextMessage(commandTypeEnum));
    }

    /**
     * Checks if the value inserted is valid
     * @return Error ID, 0 if there isn't any error
     */
    public TypeOfInputAsked validCommand (String inputReceived){
        inputReceived = inputReceived.toUpperCase();

        if (inputReceived.equals("CANCEL")){
            resetCommand();
            return new TypeOfInputAsked(commandTypeEnum, inputFormatEnum, 0, nextMessage(commandTypeEnum));
        }

        switch (commandTypeEnum) {
            case TYPEOFCHOICE:
                try {
                    setTypeOfChoice(inputReceived);
                    return new TypeOfInputAsked(commandTypeEnum, inputFormatEnum,0, nextMessage(commandTypeEnum));
                } catch (IllegalArgumentException e) {
                    return new TypeOfInputAsked(TYPEOFCHOICE, inputFormatEnum, 2001, nextMessage(commandTypeEnum));
                }

            case DICESCHEMAWHERETOTAKE:
                try {
                    setDiceSchemaWhereToTake(inputReceived);
                    return new TypeOfInputAsked(commandTypeEnum, inputFormatEnum, 0, nextMessage(commandTypeEnum));
                }catch (IllegalArgumentException e){
                    return new TypeOfInputAsked(DICESCHEMAWHERETOTAKE, InputFormatEnum.DIE_SCHEMA, 2002, nextMessage(commandTypeEnum));
                }

            case DICESCHEMAWHERETOLEAVE:
                try {
                    setDiceSchemaWhereToLeave(inputReceived);
                    return new TypeOfInputAsked(commandTypeEnum, inputFormatEnum,0, nextMessage(commandTypeEnum));
                }catch (IllegalArgumentException e){
                    return new TypeOfInputAsked(DICESCHEMAWHERETOLEAVE, InputFormatEnum.CELL_SCHEMA, 2002, nextMessage(commandTypeEnum));
                }

            case DICEBOARDINDEX:
                try{
                    setDiceBoardIndex(inputReceived);
                    return new TypeOfInputAsked(commandTypeEnum, inputFormatEnum,0, nextMessage(commandTypeEnum));
                }catch(IllegalArgumentException e){
                    return new TypeOfInputAsked(DICEBOARDINDEX, InputFormatEnum.DIE_DICEBOARD, 2003, nextMessage(commandTypeEnum));
                }

            case TRACKBOARDINDEX:
                try{
                    setTrackBoardIndex(inputReceived);
                    return new TypeOfInputAsked(commandTypeEnum, inputFormatEnum,0, nextMessage(commandTypeEnum));
                }catch(IllegalArgumentException e){
                    return new TypeOfInputAsked(TRACKBOARDINDEX, InputFormatEnum.CELL_TRACKBOARD, 2004, nextMessage(commandTypeEnum));
                }

            case TOOLCARDID:
                try{
                    setToolCardId(inputReceived, clientBoard);
                    return new TypeOfInputAsked(commandTypeEnum, inputFormatEnum,0, nextMessage(commandTypeEnum));
                }catch(IllegalArgumentException e){
                    return new TypeOfInputAsked(TOOLCARDID, InputFormatEnum.ID_TOOL, 2005, nextMessage(commandTypeEnum));
                }

            case VALUE:
                try{
                    setValue(inputReceived);
                    return new TypeOfInputAsked(commandTypeEnum, inputFormatEnum,0, nextMessage(commandTypeEnum));
                }catch(IllegalArgumentException e){
                    return new TypeOfInputAsked(VALUE, null, 2006, nextMessage(commandTypeEnum));
                }

            default:
                return new TypeOfInputAsked(commandTypeEnum, inputFormatEnum,2000, nextMessage(commandTypeEnum));
        }
    }

    private void setPassMove() {
        playerMove.setTypeOfChoice(TypeOfChoiceEnum.PASS);
    }

    private void setToolCardId(String inputReceived, ClientBoard clientBoard) {
        for (ToolCard toolCard : clientBoard.getCardOnBoard().getToolCardList()) {
            if (Integer.valueOf(inputReceived) == toolCard.getId()) {
                this.toolCard = toolCard;
                playerMove.setIdToolCard(Integer.valueOf(inputReceived));
                commandTypeEnumLists = new ArrayList<>();

                for(List<OperationString> operationStrings: toolCard.getCommandLists()){
                    List<CommandTypeEnum> commandTypeEnumList = new ArrayList<>();
                    for(OperationString operationString: operationStrings){
                        CommandTypeEnum commandTypeEnum = getCommandFromString(operationString);
                        if(commandTypeEnum != null) commandTypeEnumList.add(commandTypeEnum);
                    }
                    if(!commandTypeEnumList.isEmpty()) commandTypeEnumLists.add(commandTypeEnumList);
                }
                setNextCommandType(playerMove);

            }
        }
    }

    private CommandTypeEnum getCommandFromString(OperationString operationString) {
        switch(operationString.getOperation().toLowerCase()){
            case "pick":
                if(operationString.getDiceContainer().equalsIgnoreCase("schemacard")) return DICESCHEMAWHERETOTAKE;
                if(operationString.getDiceContainer().equalsIgnoreCase("trackboard")) return TRACKBOARDINDEX;
                if(operationString.getDiceContainer().equalsIgnoreCase("diceboard")) return DICEBOARDINDEX;
                break;

            case "incdecvalue":
                return VALUE;

            case "leave":
                if(operationString.getDiceContainer().equalsIgnoreCase("schemacard")) return DICESCHEMAWHERETOLEAVE;
                return null;

            case "exchange":
                if(operationString.getDiceContainer().equalsIgnoreCase("trackboard")) return TRACKBOARDINDEX;
                if(operationString.getDiceContainer().equalsIgnoreCase("diceboard")) return DICEBOARDINDEX;
                return null;

            case "setDieValue":
                return VALUE;

            default:
                return null;
        }
        return null;
    }

    private void setValue(String inputReceived) {
        playerMove.setValue(Integer.valueOf(inputReceived));
        setNextCommandType(playerMove);
    }

    private void setTrackBoardIndex(String inputReceived) {

        int coordinates[] = separateCellValues(inputReceived);

        if (coordinates[0] >= 0 && coordinates[0] < clientBoard.getTrackBoardDice().getDiceList().size() && coordinates[1] >= 0 &&
                coordinates[1] < clientBoard.getTrackBoardDice().getDiceList().get(coordinates[0]).size()) {

            playerMove.setTrackBoardIndex(coordinates[0], coordinates[1]);
            setNextCommandType(playerMove);
        }
    }

    private void setDiceSchemaWhereToTake(String inputReceived) {

        int coordinates[] = separateCellValues(inputReceived);

        if (coordinates[0] >= 0 && coordinates[0] < NUMBER_OF_SCHEMA_ROW && coordinates[1] >= 0 && coordinates[1] < NUMBER_OF_SCHEMA_COL) {
            playerMove.insertDiceSchemaWhereToTake(new Position(coordinates[0] , coordinates[1]));
            setNextCommandType(playerMove);

        }else throw new IllegalArgumentException("ERROR: Wrong input inserted");

    }

    private void setDiceBoardIndex(String inputReceived) {
        if (Integer.valueOf(inputReceived) >= 0 && Integer.valueOf(inputReceived) < clientBoard.getBoardDice().getDieList().size()) {
            playerMove.setDiceBoardIndex(Integer.valueOf(inputReceived));
            setNextCommandType(playerMove);
        }
    }

    private int[] separateCellValues(String message){

        int coordinates[] = {Integer.parseInt(message.split(" ")[0]),Integer.parseInt(message.split(" ")[1])};
        return coordinates;
    }

    private void setDiceSchemaWhereToLeave(String inputReceived) {

        int coordinates[] = separateCellValues(inputReceived);

        if (coordinates[0] >= 0 && coordinates[0] < NUMBER_OF_SCHEMA_ROW && coordinates[1] >= 0 && coordinates[1] < NUMBER_OF_SCHEMA_COL) {
            playerMove.insertDiceSchemaWhereToLeave(new Position(coordinates[0] , coordinates[1]));
            setNextCommandType(playerMove);

        }else throw new IllegalArgumentException("ERROR: Wrong input inserted");
    }

    private int nextMessage(CommandTypeEnum commandTypeEnum) {
        if (commandTypeEnum == COMPLETE) return 3101;
        println(inputFormatEnum + " " + commandTypeEnum);
        return 3000;
    }

    private void setTypeOfChoice(String inputReceived) {
        if (inputReceived.equalsIgnoreCase("cancel")) resetCommand();
        else playerMove.setTypeOfChoice(TypeOfChoiceEnum.valueOf(inputReceived.toUpperCase()));
        if(playerMove.getTypeOfChoice() == TypeOfChoiceEnum.TOOL && inputReceived.equalsIgnoreCase("tool")){
            commandTypeEnum = TOOLCARDID;
            return;
        }
        if(playerMove.getTypeOfChoice() == TypeOfChoiceEnum.PICK && inputReceived.equalsIgnoreCase("pick")){
            List<CommandTypeEnum> commandTypeEnumList = new ArrayList<>();
            commandTypeEnumList.add(DICEBOARDINDEX);
            commandTypeEnumList.add(DICESCHEMAWHERETOLEAVE);
            commandTypeEnumLists = new ArrayList<>();
            commandTypeEnumLists.add(commandTypeEnumList);
        }

        setNextCommandType(playerMove);
    }





    /**
     * Sets the next command type to be received
     * @param playerMove Actual status of PlayerMove
     */
    private void setNextCommandType(PlayerMove playerMove){
        if (playerMove == null || playerMove.getTypeOfChoice() == null){
            commandTypeEnum = TYPEOFCHOICE;
            return;
        }

        switch(playerMove.getTypeOfChoice()) {

            case EXTRACT:
            case ROLL:
            case PASS:
                setCompleteMove();
                return;

            case PICK:
            case TOOL:
                setNextCommandType();
                return;

            default:
                commandTypeEnum = CommandTypeEnum.TYPEOFCHOICE;
        }
    }



    private void setNextCommandType() {

        if(commandTypeEnumLists.isEmpty()){
            setCompleteMove();
            return;
        }

        commandTypeEnum = commandTypeEnumLists.get(0).remove(0);
        if(commandTypeEnumLists.get(0).isEmpty()) commandTypeEnumLists.remove(0);

    }

    /**
     * Gets Player Move
     * @return Actual status of the PlayerMove
     */
    public PlayerMove getPlayerMove(){
        return playerMove;
    }

    /**
     * Resets Command
     */
    private void resetCommand(){
        try{
            playerMove = receivedPlayerMove.getClone();
        }catch (NullPointerException e){
            playerMove = new PlayerMove();
        }

        setNextCommandType(playerMove);
    }

    private void setCompleteMove(){
        commandTypeEnum = COMPLETE;
        PlayerMessage playerMessage = new PlayerMessage();

        playerMessage.setCheckMove(playerMove);
        notify(playerMessage);
    }

}
