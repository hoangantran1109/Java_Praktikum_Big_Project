package DecksAndPiles;

import DamageCards.SPAMCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Tobias Celik, Julian Wiemer, Alina Klessinger, Ludwig Kraus
 * Class SPAMCardPile
 */
public class SPAMCardPile implements Deck {

    String name = "SPAMCardPile";

    List<Card> cards;

    public SPAMCardPile() {
        populate();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void clear() {
        this.cards.clear();
    }

    public int getSize() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }


    @Override
    public void populate() {
        cards = new ArrayList<Card>();
        addCard(new SPAMCard(), 38);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        Card topCard = cards.get(cards.size() - 1);
        cards.remove(cards.size() - 1);
        return topCard;
    }

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
