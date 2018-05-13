package it.polimi.se2018.model.cards;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Dice;
import it.polimi.se2018.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Public Class SchemaCard
 * @author Davide Gioiosa
 */

public class SchemaCard extends Card {
    private List<Cell> cellList;
    private int difficulty;
    private SchemaCard backSchema;

    /**
     * Builder: create a Scheme card
     * @param token related to the difficulty of a scheme
     */
    public SchemaCard (String name, String description, int id, int token, List<Cell> cellList){
        super(id, name, description);
        this.cellList = cellList;
        if(token < 0 || token > 6){
            throw new IllegalArgumentException("ERROR: Cannot set a value not in the range permitted");
        }
        this.difficulty = token;
    }

    /**
     * @return the difficulty of the scheme
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * insertion of the dice in the Scheme
     */
    public void setCell (Position position, Dice dice){
        if(dice == null){
            throw new IllegalArgumentException("ERROR: Selected a null die");
        }
        cellList.get(position.getIndexArrayPosition()).insertDice(dice);
    }

    /**
     * @return the list of the cells componing the scheme
     */
    public List<Cell> getCellList() {
        return cellList;
    }

    /**
     * sets the back schema of a scheme card
     * @param backSchema
     */
    public void setBackSchema(SchemaCard backSchema) {
        this.backSchema = backSchema;
    }

    /**
     * @return the scheme on the back of a scheme card
     */
    public SchemaCard getBackSchema() {
        return backSchema;
    }

    /**
     * Check if every cell of the scheme is empty
     * @return true if the all the cells are empty
     */
    public boolean isEmpty (){
        for (Cell c : cellList){
            if (!c.isEmpty()){
                return false;
            }
        }
        return true;
    }

    /**
     * @param index, number of the row
     * @return ArrayList with all the Cells that belong to that row
     */
    public List<Cell> getCellRow (int index) {
        List<Cell> rowList = new ArrayList<Cell>();
        int firstCellRow = index * 5;

        for(int i=0; i <= 4; i++){
            rowList.add(this.cellList.get(firstCellRow + i));
        }
        return rowList;
    }

    /**
     * @param index, number of the column
     * @return ArrayList with all the Cells that belong th that column
     */

    public List<Cell> getCellCol (int index) {
        List<Cell> colList = new ArrayList<Cell>();
        int firstCellCol = index;

        for(int i=0; i <= 3; i++){
            colList.add(this.cellList.get(firstCellCol + 5));
        }
        return colList;
    }

    /**
     * IF: 1. (r,c-1) sx    2. (r,c+1) dx   3. (r-1, c) up  4. (r+1, c) dwn
     * @param position
     * @return Arraylist of adjacents Cells of the position requested
     * Max num of adj for a Cell: 4
     */
    public List<Cell> getAdjacents(Position position){
        List<Cell> adjList = new ArrayList<Cell>();

        if(!this.cellList.get(position.getIndexArrayPosition()).isEmpty()){
            if(position.getCol() - 1 >= 0) {
                adjList.add(this.cellList.get(position.getIndexArrayPosition(position.getRow(), position.getCol() - 1)));
            }
            if(position.getCol() + 1 <= 4) {
                adjList.add(this.cellList.get(position.getIndexArrayPosition(position.getRow(), position.getCol() + 1)));
            }
            if(position.getRow() - 1 >= 0) {
                adjList.add(this.cellList.get(position.getIndexArrayPosition(position.getRow() - 1, position.getCol())));
            }
            if(position.getRow() + 1 <= 3) {
                adjList.add(this.cellList.get(position.getIndexArrayPosition(position.getRow() + 1, position.getCol())));
            }
        }
        return adjList;
    }

    /**
     * IF: 1. (r-1,c-1) up-sx    2. (r-1,c+1) up-dx   3. (r+1, c-1) dwn-sx  4. (r+1, c+1) dwn-dx
     * @param position
     * @return Arraylist of adjacents to the position requested
     * Max num of adj for a Cell: 4
     */
    public List<Cell> getDiagonalAdjacents(Position position){
        List<Cell> adjDiagList = new ArrayList<Cell>();

        if(!this.cellList.get(position.getIndexArrayPosition()).isEmpty()){
            if(position.getCol() - 1 >= 0 && position.getRow() - 1 >= 0) {
                adjDiagList.add(this.cellList.get(position.getIndexArrayPosition(position.getRow() - 1,
                        position.getCol() - 1)));
            }
            if(position.getRow() - 1 >= 0 && position.getCol() + 1 <= 4) {
                adjDiagList.add(this.cellList.get(position.getIndexArrayPosition(position.getRow() - 1,
                        position.getCol() + 1)));
            }
            if(position.getRow() + 1 <= 3 && position.getCol() - 1 >= 0) {
                adjDiagList.add(this.cellList.get(position.getIndexArrayPosition(position.getRow() + 1,
                        position.getCol() - 1)));
            }
            if(position.getRow() + 1 <= 3 && position.getCol() + 1 <= 4) {
                adjDiagList.add(this.cellList.get(position.getIndexArrayPosition(position.getRow() + 1,
                        position.getCol() + 1)));
            }
        }
        return adjDiagList;
    }
}