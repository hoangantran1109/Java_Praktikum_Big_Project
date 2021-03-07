package BoardTiles;

/**
 * Class Laser
 * creates the Board Tile Laser
 *
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
public class Laser extends BoardTile {

    private final String type = "laser";
    private final int priority = 5;
    private final String orientation;
    private final int count;

    /**
     * BoardLaser()
     * custom constructor
     *
     * @param orientation tile direction
     * @param count       number of lasers
     */
    public Laser(String orientation, int count) {
        this.orientation = orientation;
        this.count = count;
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
     * getOrientation()
     *
     * @return String orientation of BoardTile
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * getType()
     *
     * @return type of BoardTile (laser)
     */
    public String getType() {
        return type;
    }

    /**
     * getCount()
     *
     * @return count
     */
    public int getCount() {
        return count;
    }
}
