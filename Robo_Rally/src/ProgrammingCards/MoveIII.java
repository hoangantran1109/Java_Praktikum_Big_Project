package ProgrammingCards;

import DecksAndPiles.Card;
import GameLogic.Direction;
import GameLogic.Player;
import GameLogic.Robot;

/**
 * Class MoveIII
 * @author Alina Klessinger, Julian Wiemer, Ludwig Kraus
 */
public class MoveIII implements Card {

    private final String cardName = "MoveIII";

    public String getCardName() {
        return cardName;
    }

    public void effect(int register, Player player) {
        Robot robot = player.getRobot();

        Direction orientation = robot.orientation;


        for (int i = 0; i < 3; i++) {

            player.moveOne(orientation);
        }
    }
}