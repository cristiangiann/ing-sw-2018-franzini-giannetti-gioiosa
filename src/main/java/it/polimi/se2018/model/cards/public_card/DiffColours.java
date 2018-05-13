package it.polimi.se2018.model.cards.public_card;

import it.polimi.se2018.model.cards.SchemaCard;


import java.util.Collections;
import java.util.List;

/**
 * Public Class Different Colours
 * @author Silvia Franzini
 */
public class DiffColours implements ScoreStrategy {
    /**
     * Class implementing pattern Strategy for the evaluation of the player's score
     * due to the Public Objective Cards
     * @param schema, player's window pattern card
     * @return an int representing the player's score
     */
    @Override
    public int getScore(SchemaCard schema){

        PublicColour pub = new PublicColour();
        List<Integer> colours = pub.differentColours(schema.getCellList());
        return Collections.min(colours)*4;
    }
}