

package DecksAndPiles;

import GameLogic.Player;

/**
 * @author Tobias Celik
 * interface Card
 * This interface makes it possible to put all kinds of different cards into various cardpiles and decks
 */
public interface Card {

    String getCardName();
    void effect(int register, Player player);
}
