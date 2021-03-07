package BoardTiles;

/**
 * Class Antenna
 * creates the Board Tile Antenna
 * @Author Julian Wiemer
 */
public class Antenna extends BoardTile {

    private final String type = "antenna";

    private final boolean walkable = false;

    private final String orientation;

    public Antenna(String orientation) {
        this.orientation = orientation;
    }

    /**
     * isWalkable()
     *
     * @return ture if robot can walk and there are no obstacles
     */
    public boolean isWalkable() {
        return walkable;
    }

    /**
     * getOrientation()
     *
     * @return String Orientation of BoardTile Antenna
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * getType()
     *
     * @return type of BoardTile (Antenna)
     */
    public String getType() {
        return type;
    }
}
