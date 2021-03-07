package DecksAndPiles;

import DamageCards.VirusCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tobias Celik, Julian Wiemer, Alina Klessinger, Ludwig Kraus
 * Class VirusCardPile
 * This Class models a carddeck for the Virus Damagecards
 * @see DecksAndPiles.Deck
 */
public class VirusCardPile implements Deck {

    String name = "VirusCardPile";

    List<Card> cards;

    public VirusCardPile() {
        populate();
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public int getSize() {
        return cards.size();
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void populate() {
        cards = new ArrayList<Card>();
        addCard(new VirusCard(), 18);
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

    public void clear() {
        this.cards.clear();
    }

    @Override
    public String getName() {
        return name;
    }
}
