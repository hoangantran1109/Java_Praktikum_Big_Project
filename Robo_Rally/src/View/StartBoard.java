package View;

import BoardTiles.BoardTile;

/**
 * The interface Start board.
 *
 * @author Ludwig Kraus, Julian Wiemer, Alina Klessinger
 */
public interface StartBoard {

    BoardTile[][][] getFields();

    String getBoardType();

    int getColumns();

    BoardTile getField(int y, int x, int z);
}
