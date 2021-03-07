package BoardTiles;

/**
 * Class Belt
 * creates the Board Tile Belt
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
public class Belt extends BoardTile {

    private final String type = "belt";

    private final int speed;

    private final int priority = 1;

    private final String orientation;

    public Belt(String orientation, int speed) {
        this.orientation = orientation;
        this.speed = speed;
    }

    /**
     * getPriority()
     *
     * @return priority
     */
    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * getOrientation()
     *
     * @return String orientation of BoardTile Belt
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * getType()
     *
     * @return type of BoardTile (Belt)
     */
    public String getType() {
        return type;
    }

    /**
     * getSpeed()
     * speed == 1 -> greenConveyorBelt
     * speed == 2 -> blueConveyorBelt
     *
     * @return speed of conveyorBelt
     */
    public int getSpeed() {
        return speed;
    }
}
