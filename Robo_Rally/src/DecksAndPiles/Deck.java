package DecksAndPiles;

import java.util.List;


/**
 * @author Tobias Celik
 * Interface Deck
 * This Interface ensures that all decks implement the same methods
 */
public interface Deck {


    /**
     * getName()
     *
     * @return the name of the deck
     */
    String getName();

    /**
     * getSize()
     *
     * @return the number of cards in the deck
     */
    int getSize();

    /**
     * isEmpty()
     *
     * @return true if the deck is empty, false when it is not empty
     */
    boolean isEmpty();

    /**
     * populate()
     * fills the deck with the respective cards
     */
    void populate();

    /**
     * shuffle()
     * shuffles the deck
     */
    void shuffle();

    /**
     * drawCard()
     *
     * @return the card on top of the deck and remove the top card from the deck
     */
    Card drawCard();

    /**
     * addCard()
     *
     * @param card         the card to be added
     * @param repeatAdding how often should this card be added
     */
    void addCard(Card card, int repeatAdding);

    /**
     * getCards()
     *
     * @return all cards from the deck
     */
    List<Card> getCards();

    /**
     * clear()
     * empty the deck
     */
    void clear();

}
