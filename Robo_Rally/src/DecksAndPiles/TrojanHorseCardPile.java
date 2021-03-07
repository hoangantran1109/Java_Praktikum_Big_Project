package DecksAndPiles;

import DamageCards.TrojanHorseCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tobias Celik, Julian Wiemer, Alina Klessinger, Ludwig Kraus
 * Class TrojanHorseCardPile
 * This Class models a carddeck for the TrojanHorse Damagecards
 * @see DecksAndPiles.Deck
 */
public class TrojanHorseCardPile implements Deck {


    String name = "TrojanHorseCardPile";

    List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }

    public TrojanHorseCardPile() {
        populate();
    }

    public int getSize() {
        return cards.size();
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public void populate() {
        cards = new ArrayList<>();
        addCard(new TrojanHorseCard(), 12);
    }

    @Override
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
