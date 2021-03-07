package MessageObjects;

import java.util.List;

/**
 * class Map
 *
 * Instances of Map are used to represent a certain field position
 * in combination with a list of stacked board tiles on that position.
 * The transmitted game board consists out of a List of such instances.
 */
public class Map {
    public int position;
    public List<Field> field;
}
