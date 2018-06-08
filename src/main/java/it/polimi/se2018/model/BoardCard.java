package it.polimi.se2018.model;

import it.polimi.se2018.model.cards.ToolCard;
import it.polimi.se2018.model.cards.publiccard.PublicObjCard;

import java.io.Serializable;
import java.util.List;

/**
 * Contains a list of the cards extracted
 * @author Silvia Franzini
 */
public class BoardCard implements Serializable {
    private List<PublicObjCard> publicObjCardList;
    private List<ToolCard> toolCardList;

    /**
     * Builder method of BoardCard class
     * @param publicObjCardList List of Public Objective Cards extracted
     * @param toolCardList List of Tool Cards extracted
     */
    public BoardCard(List<PublicObjCard> publicObjCardList, List<ToolCard> toolCardList){
        if(publicObjCardList == null){
         throw new NullPointerException("ERROR: PublicObj card not existing");
        }
        if(toolCardList == null){
            throw new NullPointerException("ERROR: Tool card not existing");
        }
        this.publicObjCardList=publicObjCardList;
        this.toolCardList=toolCardList;
    }

    /**
     * Returns the list of Public Cards extracted
     * @return a list of Public Cards
     */
    public List<PublicObjCard> getPublicObjCardList() {
        return publicObjCardList;
    }

    /**
     * Returns the list of Tool Cards extracted
     * @return a list of Tool Cards
     */
    public List<ToolCard> getToolCardList() {
        return toolCardList;
    }
}
