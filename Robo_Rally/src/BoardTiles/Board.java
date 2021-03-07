package BoardTiles;

/**
 * Class Board
 * creates the Interface Board
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 */
public interface Board {

    BoardTile[][][] getFields();

    String getBoardType();

    int getCheckpoints();

    int getColumns();

    int getRows();

    BoardTile getField(int y, int x, int z);
}
