package GameLogic;

import BoardTiles.DizzyHighway;
import BoardTiles.ExtraCrispy;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

public class RobotMovementTests {

    @BeforeEach
    public void setUp() throws Exception {

        RoboRallyGame.getPlayersInGame().add(new Player("a", 1, new PrintWriter("RobotMovementTests")));
        RoboRallyGame.getPlayersInGame().add(new Player("b", 2, new PrintWriter("RobotMovementTests")));
        RoboRallyGame.getPlayersInGame().add(new Player("c", 3, new PrintWriter("RobotMovementTests")));
        RoboRallyGame.getPlayersInGame().add(new Player("d", 4, new PrintWriter("RobotMovementTests")));
        RoboRallyGame.running = true;

        GameSendingMethods.gson = new Gson();
    }

    @Test
    public void ifTwoRobotsWouldLandOnSameBlueConveyorBeltTile_ThenTheyStayOnTheirCurrentPosition() {
        RoboRallyGame.setBoard(new DizzyHighway());
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setPosition(12,1);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setPosition(11,2);

        ActivationPhaseMethods.conveyorBeltsActivate(2);

        assertEquals(12, RoboRallyGame.getPlayersInGame().get(0).getRobot().getX());
        assertEquals(1, RoboRallyGame.getPlayersInGame().get(0).getRobot().getY());
        assertEquals(11, RoboRallyGame.getPlayersInGame().get(1).getRobot().getX());
        assertEquals(2, RoboRallyGame.getPlayersInGame().get(1).getRobot().getY());
    }

    @Test
    public void ifFourAdjacentRobotsAreMovedByBlueConveyorBelt_ThenAllAreMovedCorrectly() {
        RoboRallyGame.setBoard(new DizzyHighway());
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setPosition(4,0);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setPosition(4,1);
        RoboRallyGame.getPlayersInGame().get(2).getRobot().setPosition(4,2);
        RoboRallyGame.getPlayersInGame().get(3).getRobot().setPosition(4,3);

        ActivationPhaseMethods.conveyorBeltsActivate(2);

        assertEquals(4, RoboRallyGame.getPlayersInGame().get(0).getRobot().getX());
        assertEquals(2, RoboRallyGame.getPlayersInGame().get(0).getRobot().getY());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(1).getRobot().getX());
        assertEquals(3, RoboRallyGame.getPlayersInGame().get(1).getRobot().getY());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(2).getRobot().getX());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(2).getRobot().getY());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(3).getRobot().getX());
        assertEquals(5, RoboRallyGame.getPlayersInGame().get(3).getRobot().getY());
    }

    @Test
    public void ifRobotNextToAConveyorBeltBlocksMovementOfRobotOnConveyorBelt_ThenBothStayOnTheirCurrentPosition() {
        RoboRallyGame.setBoard(new DizzyHighway());
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setPosition(2,9);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setPosition(3,9);

        ActivationPhaseMethods.conveyorBeltsActivate(1);

        assertEquals(2, RoboRallyGame.getPlayersInGame().get(0).getRobot().getX());
        assertEquals(9, RoboRallyGame.getPlayersInGame().get(0).getRobot().getY());
        assertEquals(3, RoboRallyGame.getPlayersInGame().get(1).getRobot().getX());
        assertEquals(9, RoboRallyGame.getPlayersInGame().get(1).getRobot().getY());
    }

    @Test
    public void ifOneRobotPushesAnotherRobotTwoTimes_ThenBothMoveTwoSteps() {
        RoboRallyGame.setBoard(new ExtraCrispy());
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setPosition(5,0);
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setOrientation(Direction.RIGHT);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setPosition(6,0);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setOrientation(Direction.RIGHT);

        assertTrue(RoboRallyGame.getPlayersInGame().get(0).moveOne(Direction.RIGHT));
        assertTrue(RoboRallyGame.getPlayersInGame().get(0).moveOne(Direction.RIGHT));

        assertEquals(7, RoboRallyGame.getPlayersInGame().get(0).getRobot().getX());
        assertEquals(0, RoboRallyGame.getPlayersInGame().get(0).getRobot().getY());
        assertEquals(8, RoboRallyGame.getPlayersInGame().get(1).getRobot().getX());
        assertEquals(0, RoboRallyGame.getPlayersInGame().get(1).getRobot().getY());
    }

    @Test
    public void ifFourRobotsPushEachOtherAndFirstStandsOnAWall_ThenAllStayOnTheirCurrentPosition() {
        RoboRallyGame.setBoard(new DizzyHighway());
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setPosition(1,2);
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setOrientation(Direction.UP);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setPosition(1,3);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setOrientation(Direction.UP);
        RoboRallyGame.getPlayersInGame().get(2).getRobot().setPosition(1,4);
        RoboRallyGame.getPlayersInGame().get(2).getRobot().setOrientation(Direction.UP);
        RoboRallyGame.getPlayersInGame().get(3).getRobot().setPosition(1,5);
        RoboRallyGame.getPlayersInGame().get(3).getRobot().setOrientation(Direction.UP);

        assertFalse(RoboRallyGame.getPlayersInGame().get(3).moveOne(Direction.UP));

        assertEquals(1, RoboRallyGame.getPlayersInGame().get(0).getRobot().getX());
        assertEquals(2, RoboRallyGame.getPlayersInGame().get(0).getRobot().getY());
        assertEquals(1, RoboRallyGame.getPlayersInGame().get(1).getRobot().getX());
        assertEquals(3, RoboRallyGame.getPlayersInGame().get(1).getRobot().getY());
        assertEquals(1, RoboRallyGame.getPlayersInGame().get(2).getRobot().getX());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(2).getRobot().getY());
        assertEquals(1, RoboRallyGame.getPlayersInGame().get(3).getRobot().getX());
        assertEquals(5, RoboRallyGame.getPlayersInGame().get(3).getRobot().getY());
    }

    @Test
    public void ifTwoRobotsReboot_ThenFirstRebootedRobotIsPushedInOrientationOfRebootTile() {
        RoboRallyGame.setBoard(new DizzyHighway());
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setPosition(7,0);
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setOrientation(Direction.UP);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setPosition(8,0);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setOrientation(Direction.UP);


        assertTrue(RoboRallyGame.getPlayersInGame().get(0).moveOne(Direction.UP));
        assertTrue(RoboRallyGame.getPlayersInGame().get(1).moveOne(Direction.UP));

        assertEquals(7, RoboRallyGame.getPlayersInGame().get(0).getRobot().getX());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(0).getRobot().getY());
        assertEquals(7, RoboRallyGame.getPlayersInGame().get(1).getRobot().getX());
        assertEquals(3, RoboRallyGame.getPlayersInGame().get(1).getRobot().getY());

        assertEquals(Direction.UP, RoboRallyGame.getPlayersInGame().get(0).getRobot().getOrientation());
        assertEquals(Direction.UP, RoboRallyGame.getPlayersInGame().get(1).getRobot().getOrientation());
    }
}
