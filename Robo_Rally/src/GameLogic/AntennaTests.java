/**
 * @author Ludwig Kraus, Julian Wiemer
 * <p>
 * class AntennaTests
 * contains test affirming that the players are prioriteyed correctly
 */

package GameLogic;

import BoardTiles.DizzyHighway;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AntennaTests {

    @BeforeEach
    public void setUp() throws Exception {

        RoboRallyGame.getPlayersInGame().add(new Player("a", 1, new PrintWriter("AntennaTests")));
        RoboRallyGame.getPlayersInGame().add(new Player("b", 2, new PrintWriter("AntennaTests")));
        RoboRallyGame.getPlayersInGame().add(new Player("c", 3, new PrintWriter("AntennaTests")));
        RoboRallyGame.getPlayersInGame().add(new Player("d", 4, new PrintWriter("AntennaTests")));
        RoboRallyGame.running = true;

        GameSendingMethods.gson = new Gson();
    }


    @Test
    public void testSecondPlayerHasPriority() {
        RoboRallyGame.setBoard(new DizzyHighway());
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setPosition(0, 3);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setPosition(0, 5);

        assertEquals(true, RoboRallyGame.secondPlayerHasPriority(RoboRallyGame.getPlayersInGame().get(0),
                RoboRallyGame.getPlayersInGame().get(1)));
        assertEquals(false, RoboRallyGame.secondPlayerHasPriority(RoboRallyGame.getPlayersInGame().get(1),
                RoboRallyGame.getPlayersInGame().get(0)));

        RoboRallyGame.getPlayersInGame().clear();

    }


    @Test
    public void sortingWhenRobotsHaveDifferentDistancesToAntenna() {
        RoboRallyGame.setBoard(new DizzyHighway());
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setPosition(10, 8); //a distance 14
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setPosition(3, 3);  //b distance 4
        RoboRallyGame.getPlayersInGame().get(2).getRobot().setPosition(0, 6);  //c distance 2
        RoboRallyGame.getPlayersInGame().get(3).getRobot().setPosition(0, 3);  //d distance 1


        List<Player> orderedPlayers = RoboRallyGame.prioritizePlayers(RoboRallyGame.getPlayersInGame());
        assertEquals(14, RoboRallyGame.getPlayersInGame().get(0).getRobot().getDistanceToAntenna());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(1).getRobot().getDistanceToAntenna());
        assertEquals(2, RoboRallyGame.getPlayersInGame().get(2).getRobot().getDistanceToAntenna());
        assertEquals(1, RoboRallyGame.getPlayersInGame().get(3).getRobot().getDistanceToAntenna());

        // sorted order should be d, c, b, a
        assertEquals("d", orderedPlayers.get(0).getName());
        assertEquals("c", orderedPlayers.get(1).getName());
        assertEquals("b", orderedPlayers.get(2).getName());
        assertEquals("a", orderedPlayers.get(3).getName());

        RoboRallyGame.getPlayersInGame().clear();
    }



    @Test
    public void sortingWhenTwoRobotsHaveEqualDistancesToAntenna() {
        RoboRallyGame.setBoard(new DizzyHighway());
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setPosition(0, 0);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setPosition(2, 1);
        RoboRallyGame.getPlayersInGame().get(2).getRobot().setPosition(5, 4);
        RoboRallyGame.getPlayersInGame().get(3).getRobot().setPosition(0, 8);

        List<Player> newOrderedPlayers = RoboRallyGame.prioritizePlayers(RoboRallyGame.getPlayersInGame());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(0).getRobot().getDistanceToAntenna());
        assertEquals(5, RoboRallyGame.getPlayersInGame().get(1).getRobot().getDistanceToAntenna());
        assertEquals(5, RoboRallyGame.getPlayersInGame().get(2).getRobot().getDistanceToAntenna());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(3).getRobot().getDistanceToAntenna());

        // sorted order should be c, d, a, b
        assertEquals("d", newOrderedPlayers.get(0).getName());
        assertEquals("a", newOrderedPlayers.get(1).getName());
        assertEquals("c", newOrderedPlayers.get(2).getName());
        assertEquals("b", newOrderedPlayers.get(3).getName());

        RoboRallyGame.getPlayersInGame().clear();
    }

    @Test
    public void sortingWhenFourRobotsHaveEqualDistancesToAntenna() {
        RoboRallyGame.setBoard(new DizzyHighway());
        RoboRallyGame.getPlayersInGame().get(0).getRobot().setPosition(0, 0);
        RoboRallyGame.getPlayersInGame().get(1).getRobot().setPosition(2, 2);
        RoboRallyGame.getPlayersInGame().get(2).getRobot().setPosition(4, 4);
        RoboRallyGame.getPlayersInGame().get(3).getRobot().setPosition(0, 8);

        List<Player> newOrderedPlayers = RoboRallyGame.prioritizePlayers(RoboRallyGame.getPlayersInGame());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(0).getRobot().getDistanceToAntenna());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(1).getRobot().getDistanceToAntenna());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(2).getRobot().getDistanceToAntenna());
        assertEquals(4, RoboRallyGame.getPlayersInGame().get(3).getRobot().getDistanceToAntenna());

        // sorted order should be c, d, a, b
        assertEquals("c", newOrderedPlayers.get(0).getName());
        assertEquals("d", newOrderedPlayers.get(1).getName());
        assertEquals("a", newOrderedPlayers.get(2).getName());
        assertEquals("b", newOrderedPlayers.get(3).getName());

        RoboRallyGame.getPlayersInGame().clear();
    }
}

