package BoardTiles;

/**
 * Class Gear
 * creates the Board Tile Gear
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
public class Gear extends BoardTile {

    private final String type = "gear";
    private final int priority = 4;
    private final String orientation;
    public Gear(String orientation) {
        this.orientation = orientation;
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
     * getType()
     *
     * @return type of BoardTile (gear)
     */
    public String getType() {
        return type;
    }

    /**
     * getOrientation()
     *
     * @return String orientation of BoardTile
     */
    public String getOrientation() {
        return orientation;
    }
}
