package ProgrammingCards;

import DecksAndPiles.Card;
import GameLogic.Player;

/**
 * Class PowerUp
 * @author Alina Klessinger, Julian Wiemer, Ludwig Kraus
 */
public class PowerUp implements Card {

    private final String cardName = "PowerUp";

    public String getCardName() {
        return cardName;
    }

    public void effect(int register, Player player){
        player.increaseEnergyCubes(1);
    }
}