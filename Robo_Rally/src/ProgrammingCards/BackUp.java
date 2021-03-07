package ProgrammingCards;

import DecksAndPiles.Card;
import GameLogic.Direction;
import GameLogic.Player;

/**
 * Class BackUp
 * @author Alina Klessinger, Julian Wiemer, Ludwig Kraus
 */
public class BackUp implements Card {

    private final String cardName = "BackUp";

    public String getCardName() {
        return cardName;
    }
    public void effect(int register, Player player) {
        Direction orientation = player.getRobot().orientation;

        player.moveOne(orientation.getOpposite());

    }
}