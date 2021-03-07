
package ProgrammingCards;

import DecksAndPiles.Card;
import GameLogic.Player;

/**
 * Class Again
 * @author Alina Klessinger, Julian Wiemer, Ludwig Kraus
 */
public class Again implements Card {

    private final String cardName = "Again";

    public String getCardName() {
        return cardName;
    }

    /**
     * effect()
     * @param register
     * @param player
     * implements the effect of the again Card
     */
    public void effect(int register, Player player) {
        if (register > 0) {
            player.getProgrammingCardRegister().get(register - 1).effect(register - 1, player);
        }
    }
}