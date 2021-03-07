package DecksAndPiles;

import ProgrammingCards.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tobias Celik, Julian Wiemer, Alina Klessinger, Ludwig Kraus
 * Class ProgrammingDeck
 * This Class models a carddeck for the Programming Cards
 * @see DecksAndPiles.Deck
 */
public class ProgrammingDeck implements Deck {


    String name = "ProgrammingDeck";

    private List<Card> cards;


    public void clear() {
        this.cards.clear();
    }


    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getSize() {
        return this.cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void populate() {
        this.cards = new ArrayList<>();
        addCard(new MoveI(), 5);
        addCard(new MoveII(), 3);
        addCard(new MoveIII(), 1);
        addCard(new TurnRight(), 3);
        addCard(new TurnLeft(), 3);
        addCard(new UTurn(), 1);
        addCard(new BackUp(), 1);
        addCard(new PowerUp(), 1);
        addCard(new Again(), 2);
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        Card topCard = cards.get(cards.size() - 1);
        this.cards.remove(cards.size() - 1);
        return topCard;
    }

    public void addCard(Card card, int repeatAdding) {
        for (int i = 0; i < repeatAdding; i++) {
            this.cards.add(card);
        }
    }

    /**
     * setCard()
     *
     * @param index where the card should be set
     * @param card  the card that should be set
     *              sets a card on a certain index in the deck
     */
    public void setCard(int index, Card card) {
        this.cards.add(index, card);
    }

    @Override
    public String getName() {
        return name;
    }
}