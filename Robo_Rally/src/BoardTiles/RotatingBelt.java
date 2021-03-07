package BoardTiles;

import java.util.List;

/**
 * Class RotatingBelt
 * creates the Board Tile RotatingBelt
 *
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
public class RotatingBelt extends BoardTile {

    private final String type = "rotatingBelt";
    private final int priority = 2;
    private final List<String> orientations;
    private final int speed;
    private final boolean isCrossing;

    /**
     * RotatingBelt()
     *
     * @param orientations list of direction from which is rotated and from which the robot comes
     * @param speed        decides wether it is a blue (2) or a green (1) belt
     * @param isCrossing   decides wether the board tile is a intersection
     */
    public RotatingBelt(List<String> orientations, int speed, boolean isCrossing) {
        this.orientations = orientations;
        this.speed = speed;
        this.isCrossing = isCrossing;
    }

    /**
     * getPriority()
     *
     * @return int priority of BoardTile
     */
    @Override
    public int getPriority() {
        return priority;
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
     * getType()
     *
     * @return type of BoardTile (rotatingBelt)
     */
    public String getType() {
        return type;
    }

    /**
     * getSpeed()
     *
     * @return 1 for green belt or 2 for blue belt
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * isCrossing()
     *
     * @return boolean crossing
     */
    public boolean isCrossing() {
        return isCrossing;
    }
}
