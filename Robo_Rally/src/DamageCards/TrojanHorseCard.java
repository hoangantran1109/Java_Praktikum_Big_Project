package DamageCards;

import DecksAndPiles.Card;
import GameLogic.ActivationPhaseMethods;
import GameLogic.Player;
import GameLogic.RoboRallyGame;

import static GameLogic.GameSendingMethods.sendShuffleCodingMessage;

/**
 * @author Ludwig Kraus, Simon Wiethase, Julian Wiemer, Alina Klessinger
 * class TrojanHorseCard
 * implements the Trojan Horse-DamageCard
 */
public class TrojanHorseCard implements Card {

    private final String cardName = "Trojan Horse";

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
     * implements the effect of the trojan horse card
     * a player who programs this card has to draw two  SPAMcards
     *
     * @param register in which the card lies during the activationphase
     * @param player   player who played the card
     */
    public void effect(int register, Player player) {
        ActivationPhaseMethods.drawDamageAndSendDamageMessage(player, RoboRallyGame.getSpamCardPile(), 2);
        RoboRallyGame.getTrojanHorseCardPile().addCard(this, 1);
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


