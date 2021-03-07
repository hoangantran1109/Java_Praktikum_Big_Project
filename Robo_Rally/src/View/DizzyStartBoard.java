package View;

import BoardTiles.*;

import java.util.Collections;

/**
 * Class CrispyStartBoard
 * This Class models the StartBoard for the DizzyHighway Map.
 * This Board is already locally stored in the Client, the Server only has to send the Map of the Game
 *
 * @author Ludwig Kraus, Julian Wiemer, Alina Klessinger
 */

public class DizzyStartBoard implements StartBoard {

    private final int columns = 3;
    private final int rows = 10;
    private final int layers = 2;


    private final BoardTile[][][] dizzyStartBoard = new BoardTile[rows][columns][layers];

    public DizzyStartBoard() {

        //line 1:
        dizzyStartBoard[0][0][0] = new Empty();
        dizzyStartBoard[0][1][0] = new Empty();
        dizzyStartBoard[0][2][0] = new Belt("right", 1);


        //line 2:
        dizzyStartBoard[1][0][0] = new Empty();
        dizzyStartBoard[1][1][0] = new StartPoint();
        dizzyStartBoard[1][2][0] = new Empty();


        //line 3:
        dizzyStartBoard[2][0][0] = new Empty();
        dizzyStartBoard[2][1][0] = new Wall(Collections.singletonList("up"));
        dizzyStartBoard[2][2][0] = new Empty();


        //line 4:
        dizzyStartBoard[3][0][0] = new StartPoint();
        dizzyStartBoard[3][1][0] = new Empty();
        dizzyStartBoard[3][2][0] = new Empty();


        //line 5:
        dizzyStartBoard[4][0][0] = new Antenna("right");
        dizzyStartBoard[4][1][0] = new StartPoint();
        dizzyStartBoard[4][2][0] = new Wall(Collections.singletonList("right"));


        //line 6:
        dizzyStartBoard[5][0][0] = new Empty();
        dizzyStartBoard[5][1][0] = new StartPoint();
        dizzyStartBoard[5][2][0] = new Wall(Collections.singletonList("right"));


        //line 7:
        dizzyStartBoard[6][0][0] = new StartPoint();
        dizzyStartBoard[6][1][0] = new Empty();
        dizzyStartBoard[6][2][0] = new Empty();


        //line 8:
        dizzyStartBoard[7][0][0] = new Empty();
        dizzyStartBoard[7][1][0] = new Wall(Collections.singletonList("down"));
        dizzyStartBoard[7][2][0] = new Empty();


        //line 9:
        dizzyStartBoard[8][0][0] = new Empty();
        dizzyStartBoard[8][1][0] = new StartPoint();
        dizzyStartBoard[8][2][0] = new Empty();


        //line 10:
        dizzyStartBoard[9][0][0] = new Empty(); // Board_Identifiers A
        dizzyStartBoard[9][1][0] = new Empty();
        dizzyStartBoard[9][2][0] = new Belt("right", 1);

    }

    /**
     * getFields()
     *
     * @return fields
     */
    @Override
    public BoardTile[][][] getFields() {
        return dizzyStartBoard;
    }

    /**
     * getBoardType()
     *
     * @return type of map (DizzyHighway)
     */
    @Override
    public String getBoardType() {
        return "DizzyStartBoard";
    }

    /**
     * getColumns()
     *
     * @return columns
     */
    @Override
    public int getColumns() {
        return columns;
    }

    /**
     * getField()
     *
     * @param y rows
     * @param x columns
     * @param z layer
     * @return BoardTile
     */
    @Override
    public BoardTile getField(int y, int x, int z) {
        return dizzyStartBoard[y][x][z];
    }
}
