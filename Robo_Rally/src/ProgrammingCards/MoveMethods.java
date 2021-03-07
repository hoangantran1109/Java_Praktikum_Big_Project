package ProgrammingCards;

import BoardTiles.Antenna;
import BoardTiles.BoardTile;
import BoardTiles.Wall;
import GameLogic.Direction;

import java.util.List;

/**
 * Class MoveMethods
 * @author Alina Klessinger, Julian Wiemer, Ludwig Kraus
 */
public abstract class MoveMethods {
    /**
     * boolean checkForObstacles() - checks if there's any static obstacle (wall, antenna,...) that blocks a robot to
     * moveOne() in a certain direction
     * @param field current field of the current robot
     * @param nextField the field to walk on
     * @param orientation moving direction of the current robot
     * @return true, if there aren't any obstacles, false, if there are.
     */
    public static boolean freeOfObstacles(BoardTile field, BoardTile nextField, Direction orientation) {
        if(nextField == null) {
            return true;
        }
        Direction oppositeDirection = orientation.getOpposite();

        String directionString = orientation.directionToString();
        String oppositeDirectionString = oppositeDirection.directionToString();

        boolean pass = true;

        boolean currentFieldHasWall = field.getClass().equals(Wall.class);
        boolean nextFieldHasWall = nextField.getClass().equals(Wall.class);
        boolean nextFieldHasAntenna = nextField.getClass().equals(Antenna.class);
//        boolean nextFieldHasPushPanel = nextField.getClass().equals(PushPanel.class);

        if (currentFieldHasWall) {
            List<String> wallPosition = field.getOrientations();
            if (wallPosition.contains(directionString)) {
                pass = false;
            }
        }
        if (pass & nextFieldHasAntenna) {
            pass = false;
        }
//        if (pass && nextFieldHasPushPanel) {
//            String pushPanelPosition = ((PushPanel) nextField).getOrientation();
//            if(pushPanelPosition.equals(directionString)) {
//                pass = false;
//            }
//        }
        if (pass && nextFieldHasWall) {
            List<String> wallPosition = nextField.getOrientations();
            if (wallPosition.contains(oppositeDirectionString)) {
                pass = false;
            }
        }
        return pass;
    }





    protected int getDirectionInt(Direction direction) {
        return switch (direction) {
            case UP -> 0;
            case RIGHT -> 1;
            case DOWN -> 2;
            case LEFT -> 3;
        };
    }

}
