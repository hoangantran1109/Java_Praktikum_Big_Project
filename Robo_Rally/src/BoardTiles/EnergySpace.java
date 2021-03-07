package BoardTiles;

/**
 * Class EnergySpace
 * creates the Board Tile Energy Space
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
public class EnergySpace extends BoardTile {

    private final String type = "energySpace";
    private int count;
    private final int priority = 7;
    public EnergySpace(int count) {
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
     * getType()
     *
     * @return type of BoardTile (energySpace)
     */
    @Override
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

    /**
     * reduceCount()
     * reduces variable count
     */
    public void reduceCount() {
        this.count--;
    }

}
