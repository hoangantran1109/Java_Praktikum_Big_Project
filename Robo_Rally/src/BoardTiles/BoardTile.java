package BoardTiles;

import java.util.Arrays;
import java.util.List;

/**
 * Class BoardTile
 * creates the abstract class BoardTile
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
public abstract class BoardTile {

    private final int priority = 0;

    private final boolean walkable = true;

    private final List<String> orientations = Arrays.asList("up");

    private String orientation;

    private final int speed = 0;

    private int count;

    /**
     * getPriority() - returns tile-specific priority
     *
     * @return priority 0 by default
     */
    public int getPriority() {
        return priority;
    }

    /**
     * isWalkable() - indicates if tile is walkable
     *
     * @return walkable true by default
     */
    public boolean isWalkable() {
        return walkable;
    }

    /**
     * getType()
     *
     * @return type of BoardTile
     */
    public abstract String getType();

    /**
     * getSpeed()
     *
     * @return speed of conveyorBelt
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * getOrientation()
     *
     * @return String orientation of BoardTile
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * reduceCount()
     * reduces the variable count
     */
    public void reduceCount() {
    }

    /**
     * getCount()
     *
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * getOrientations()
     * gets the orientations of rotating belts
     *
     * @return List of Orientations
     */
    public List<String> getOrientations() {
        return orientations;
    }

    /**
     * isCrossing()
     *
     * @return boolean crossing
     */
    public boolean isCrossing() {
        return false;
    }
}
