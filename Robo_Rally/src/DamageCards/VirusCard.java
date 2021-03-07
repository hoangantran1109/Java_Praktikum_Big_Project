package DamageCards;

import DecksAndPiles.Card;
import GameLogic.ActivationPhaseMethods;
import GameLogic.Player;
import GameLogic.RoboRallyGame;

import static GameLogic.GameSendingMethods.sendShuffleCodingMessage;

/**
 * @author Ludwig Kraus, Simon Wiethase, Julian Wiemer, Alina Klessinger
 * class VirusCard
 * implements the Virus-DamageCard
 */
public class VirusCard implements Card {

    private final String cardName = "Virus";

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
     * implements the effect of the virus card
     * when a player programs this card all other players within a 13*13 square around the player
     * have to draw a virus card
     *
     * @param register in which the card lies during the activationphase
     * @param player   player who played the card
     */
    public void effect(int register, Player player) {
        int upperLeftCornerOfSquareX = player.getRobot().getX() - 6;
        if (upperLeftCornerOfSquareX < 0) {
            upperLeftCornerOfSquareX = 0;
        }
        int bottomRightCornerOfSquareX = player.getRobot().getX() + 6;
        if (bottomRightCornerOfSquareX > 12) {
            bottomRightCornerOfSquareX = 12;
        }
        int upperLeftCornerOfSquareY = player.getRobot().getY() - 6;
        if (upperLeftCornerOfSquareY < 0) {
            upperLeftCornerOfSquareY = 0;
        }
        int bottomRightCornerOfSquareY = player.getRobot().getY() + 6;
        if (bottomRightCornerOfSquareY > 9) {
            bottomRightCornerOfSquareY = 9;
        }

        for (int x = upperLeftCornerOfSquareX; x <= bottomRightCornerOfSquareX; x++) {
            for (int y = upperLeftCornerOfSquareY; y <= bottomRightCornerOfSquareY; y++) {
                Player targetPlayer = ActivationPhaseMethods.getPlayerOnField(x, y);
                if (targetPlayer != null && targetPlayer != player) {
                    ActivationPhaseMethods.drawDamageAndSendDamageMessage(targetPlayer, RoboRallyGame.getVirusCardPile(), 1);
                }
            }
        }
        RoboRallyGame.getVirusCardPile().addCard(this, 1);
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