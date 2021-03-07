package MessageObjects;
import DecksAndPiles.Card;

/**
 * class ActiveCard
 *
 * provides custom (playerID, card) - pairs
 * for messageType CurrentCards
 */
public class ActiveCard {
    public int playerId;
    public String card;

    /**
     * ActiveCard()
     * @param playerId ID of current player
     * @param card this player's currently active card
     */
    public ActiveCard(int playerId, Card card) {
        this.playerId = playerId;
        this.card = card.getCardName();
    }
}
