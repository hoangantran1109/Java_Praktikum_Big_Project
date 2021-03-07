package DamageCards;

import DecksAndPiles.Card;
import GameLogic.Player;
import GameLogic.RoboRallyGame;

import static GameLogic.GameSendingMethods.sendShuffleCodingMessage;

/**
 * @author Ludwig Kraus, Simon Wiethase, Julian Wiemer, Alina Klessinger
 * class WormCard
 * implements the Worm-DamageCard
 */
public class WormCard implements Card {

    private final String cardName = "Worm";

    /**
     * getCardName()
     *
     * @return the name of the card
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * effect()
     * implements the effect of the worm card
     * when a player programs this card he has two reboot immediately
     *
     * @param register in which the card lies during the activationphase
     * @param player   player who played the card
     */
    public void effect(int register, Player player) {
        player.reboot();
        RoboRallyGame.getWormCardPile().addCard(this, 1);
        if (player.getProgrammingDeck().getSize() == 0) {
            player.getProgrammingDiscardPile().shuffle();
            sendShuffleCodingMessage(player);
            int discardPileSize = player.getProgrammingDiscardPile().getSize();
            for (int j = 0; j < discardPileSize; j++) {
                player.getProgrammingDeck().addCard(player.getProgrammingDiscardPile().drawCard(), 1);
            }
        }
        player.getProgrammingCardRegister().put(register, player.getProgrammingDeck().drawCard());
    }
}

