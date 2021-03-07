package ProgrammingCards;

import DecksAndPiles.Card;
import GameLogic.Direction;
import GameLogic.Player;
import GameLogic.Robot;

/**
 * Class MoveII
 * @author Alina Klessinger, Julian Wiemer, Ludwig Kraus
 */
public class MoveII implements Card {

    private final String cardName = "MoveII";

    public String getCardName() {
        return cardName;
    }

    public void effect(int register, Player player) {

        Robot robot = player.getRobot();

        Direction orientation = robot.orientation;

        for (int i = 0; i < 2; i++) {
            player.moveOne(orientation);
        }
    }
}