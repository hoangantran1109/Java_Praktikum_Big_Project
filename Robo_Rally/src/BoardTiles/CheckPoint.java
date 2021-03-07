package BoardTiles;

/**
 * Class CheckPoint
 * creates the Board Tile CheckPoint
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
public class CheckPoint extends BoardTile {

    private final String type = "checkPoint";

    private final int count;

    private final int priority = 8;

    public CheckPoint(int count) {
        this.count = count;
    }

    /**
     * getPriority()
     *
     * @return priority of BoardTile
     */
    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * getType()
     *
     * @return type of BoardTile (CheckPoint)
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
