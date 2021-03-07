package View;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Simon Wiethase
 */
public class AITests {

    private final Map<Integer, String> allWallsOnMap = new HashMap<>();

    @BeforeEach
    public void setUp() {
        allWallsOnMap.put(92, "down");
    }

    @Test
    public void ifMovesFromBelowIntoDownWall_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"TurnLeft", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "right", 105, allWallsOnMap));
    }

    @Test
    public void ifMovesFromAboveIntoDownWall_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"TurnRight", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "right", 92, allWallsOnMap));
    }

    @Test
    public void ifMovesPastWall_ItMovesCorrectly() {
        String[] cardsToTest = new String[]{"MoveI", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(84, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "right", 79, allWallsOnMap));
    }

    @Test
    public void ifMovesRightAcrossMap_ItMovesCorrectly() {
        String[] cardsToTest = new String[]{"MoveI", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(20, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "right", 15, allWallsOnMap));
    }

    @Test
    public void ifMovesDownAcrossMap_ItMovesCorrectly() {
        String[] cardsToTest = new String[]{"MoveI", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(80, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "down", 15, allWallsOnMap));
    }

    @Test
    public void ifMovesLeftAcrossMap_ItMovesCorrectly() {
        String[] cardsToTest = new String[]{"MoveI", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(8, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "left", 13, allWallsOnMap));
    }
    @Test
    public void ifMovesUpAcrossMap_ItMovesCorrectly() {
        String[] cardsToTest = new String[]{"MoveI", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(125, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "left", 130, allWallsOnMap));
    }

    @Test
    public void ifMovesDownOutsideMap_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"TurnRight", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "right", 105, allWallsOnMap));
    }

    @Test
    public void ifTurnsLeftMovesDownOutsideMap_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"TurnLeft", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "left", 105, allWallsOnMap));
    }

    @Test
    public void ifMovesLeftOutsideMap_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"MoveI", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "left", 105, allWallsOnMap));
    }

    @Test
    public void ifMovesRightOutsideMapOneStep_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"MoveI", "someCard", "someCard", "someCard", "someCard"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "right", 13, allWallsOnMap));
    }

    @Test
    public void ifMovesUpOutsideMapOneStep_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"MoveI", "someCard", "someCard", "someCard", "someCard"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "up", 13, allWallsOnMap));
    }

    @Test
    public void ifMovesDownOutsideMapOneStep_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"MoveI", "someCard", "someCard", "someCard", "someCard"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "down", 129, allWallsOnMap));
    }

    @Test
    public void ifMovesDownOutsideMapWithThree_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"someCard", "someCard", "someCard", "someCard", "MoveIII"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "down", 104, allWallsOnMap));
    }

    @Test
    public void ifMovesLeftOutsideMapOneStep_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"MoveI", "someCard", "someCard", "someCard", "someCard"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "left", 14, allWallsOnMap));
    }

    @Test
    public void ifMovesRightOutsideMap_ItReturnsCorrectStatusCode() {
        String[] cardsToTest = new String[]{"MoveI", "MoveI", "MoveI", "MoveI", "MoveI"};
        assertEquals(-99, AI.getInstance().getEndPositionOfFiveCards(cardsToTest, "right", 12, allWallsOnMap));
    }
}