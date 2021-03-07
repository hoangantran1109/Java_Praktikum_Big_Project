package ProgrammingCards;

import DecksAndPiles.Card;
import GameLogic.Player;
import GameLogic.Robot;

/**
 * Class TurnRight
 * @author Alina Klessinger, Julian Wiemer, Ludwig Kraus
 */
public class TurnRight implements Card {

    private final String cardName = "TurnRight";

    public String getCardName() {
        return cardName;
    }

    public void effect(int register, Player player){
        Robot robot = player.getRobot();
        robot.rotate(player,true, true);
    }
}