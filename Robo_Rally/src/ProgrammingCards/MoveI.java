package ProgrammingCards;

import DecksAndPiles.Card;
import GameLogic.Direction;
import GameLogic.Player;
import GameLogic.Robot;

/**
 * Class MoveI
 * @author Alina Klessinger, Julian Wiemer, Ludwig Kraus
 */
public class MoveI implements Card {

    private final String cardName = "MoveI";

    public String getCardName() {
        return cardName;
    }
    public void effect(int register, Player player) {
        Robot robot = player.getRobot();

        Direction orientation = robot.orientation;

        player.moveOne(orientation);
    }
}
