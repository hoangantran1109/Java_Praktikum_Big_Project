package BoardTiles;

/**
 * Class PushPanel
 * creates the Board Tile PushPanel
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
import java.util.List;

public class PushPanel extends BoardTile {

    private final String type = "pushPanel";
    private final int priority = 3;
    private final String orientation;
    private final List<Integer> registers;

    /**
     * PushPanel()
     * pushes any robots resting on them into the next space in the direction the push panel faces.
     * @param orientation orientation the push panel faces
     * @param registers list of register
     */
    public PushPanel(String orientation, List<Integer> registers) {
        this.orientation = orientation;
        this.registers = registers;
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
     * @return type of BoardTile (pushPanel)
     */
    public String getType() {
        return type;
    }

    /**
     * getRegisters()
     *
     * @return List of register indices
     */
    public List<Integer> getRegisters() {
        return registers;
    }
}
