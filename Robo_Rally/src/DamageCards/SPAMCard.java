package DamageCards;

import DecksAndPiles.Card;
import GameLogic.Player;
import GameLogic.RoboRallyGame;

import static GameLogic.GameSendingMethods.sendShuffleCodingMessage;

/**
 * @author Ludwig Kraus, Simon Wiethase, Julian Wiemer, Alina Klessinger
 * class SPAMCard
 * implements the SPAM-DamageCard
 */
public class SPAMCard implements Card {

    private final String cardName = "SPAM";

    /**
     * getCardName()
     *
     * @return name of card
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * effect()
     * implements the effect of the SPAM card (which is none)
     *
     * @param register in which the card lies during the activationphase
     * @param player   player who played the card
     */
    public void effect(int register, Player player) {
        RoboRallyGame.getSpamCardPile().addCard(this,1);
        if (player.getProgrammingDeck().getSize() == 0) {
            player.getProgrammingDiscardPile().shuffle();
            sendShuffleCodingMessage(player);
            int discardPileSize = player.getProgrammingDiscardPile().getSize();
            for (int j = 0; j < discardPileSize; j++) {
                player.getProgrammingDeck().addCard(player.getProgrammingDiscardPile().drawCard(), 1);
            }
        }
        player.getProgrammingCardRegister().put(register, player.getProgrammingDeck().drawCard());
        player.getProgrammingCardRegister().get(register).effect(register, player);
    }
}