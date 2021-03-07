package BoardTiles;

/**
 * Class RestartPoint
 * creates the Board Tile RestartPoint
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
public class RestartPoint extends BoardTile {

    private final String type = "restartPoint";
    private final String orientation;
    public RestartPoint(String orientation) {
        this.orientation = orientation;
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
     * @return type of BoardTile (restartPoint)
     */
    public String getType() {
        return type;
    }
}
