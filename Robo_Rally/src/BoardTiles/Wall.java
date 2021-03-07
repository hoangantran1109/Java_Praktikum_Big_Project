package BoardTiles;

import java.util.List;

/**
 * Class Wall
 * creates the Board Tile Wall
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
public class Wall extends BoardTile {

    private final String type = "wall";

    private final List<String> orientations;

    /**
     * Wall() - custom constructor
     *
     * @param orientations (optional) Direction of second wall
     */
    public Wall(List<String> orientations) {
        this.orientations = orientations;
    }

    public List<String> getOrientations() {
        return orientations;
    }

    public String getType() {
        return type;
    }
}
