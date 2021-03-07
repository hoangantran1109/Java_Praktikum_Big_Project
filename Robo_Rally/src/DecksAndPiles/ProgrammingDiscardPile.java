package DecksAndPiles;

import java.util.*;

/**
 * @author Tobias Celik, Julian Wiemer, Alina Klessinger, Ludwig Kraus
 * Class ProgrammingDiscardPile
 */
public class ProgrammingDiscardPile implements Deck {

    String name = "ProgrammingDiscardPile";

    List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public ProgrammingDiscardPile() {
        populate();
    }

    @Override
    public int getSize() {
        return cards.size();
    }

    @Override
    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public void populate() {
        cards = new ArrayList<Card>();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        Card topCard = cards.get(cards.size() - 1);
        this.cards.remove(cards.size() - 1);
        return topCard;
    }

    public void clear() {
        this.cards.clear();
    }

    @Override
    public void addCard(Card card, int repeatAdding) {
        for (int i = 0; i < repeatAdding; i++) {
            cards.add(card);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
