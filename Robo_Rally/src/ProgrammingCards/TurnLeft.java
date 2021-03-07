package ProgrammingCards;

import DecksAndPiles.Card;
import GameLogic.Player;
import GameLogic.Robot;

/**
 * Class TurnLeft
 * @author Alina Klessinger, Julian Wiemer, Ludwig Kraus
 */
public class TurnLeft implements Card {

    private final String cardName = "TurnLeft";

    public String getCardName() {
        return cardName;
    }

    public void effect(int register, Player player){
        Robot robot = player.getRobot();
        robot.rotate(player,false, true);
    }
}