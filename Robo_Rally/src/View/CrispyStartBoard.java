package View;

import BoardTiles.*;

import java.util.Collections;

/**
 * Class CrispyStartBoard
 * This Class models the StartBoard for the ExtraCrispy Map.
 * This Board is already locally stored in the Client, the Server only has to send the Map of the Game
 *
 * @author Ludwig Kraus, Julian Wiemer, Alina Klessinger
 */
public class CrispyStartBoard implements StartBoard {

    private final int columns = 3;
    private final int rows = 10;
    private final int layers = 2;


    private final BoardTile[][][] crispyStartBoard = new BoardTile[rows][columns][layers];

    public CrispyStartBoard() {

        //line 1:
        crispyStartBoard[0][0][0] = new RestartPoint("up");
        crispyStartBoard[0][1][0] = new Empty();
        crispyStartBoard[0][2][0] = new Belt("right", 1);


        //line 2: Done
        crispyStartBoard[1][0][0] = new Empty();
        crispyStartBoard[1][1][0] = new StartPoint();
        crispyStartBoard[1][2][0] = new Empty();


        //line 3:
        crispyStartBoard[2][0][0] = new Empty();
        crispyStartBoard[2][1][0] = new Wall(Collections.singletonList("up"));
        crispyStartBoard[2][2][0] = new Empty();


        //line 4:
        crispyStartBoard[3][0][0] = new StartPoint();
        crispyStartBoard[3][1][0] = new Empty();
        crispyStartBoard[3][2][0] = new Empty();


        //line 5:
        crispyStartBoard[4][0][0] = new Antenna("right");
        crispyStartBoard[4][1][0] = new StartPoint();
        crispyStartBoard[4][2][0] = new Wall(Collections.singletonList("right"));


        //line 6:
        crispyStartBoard[5][0][0] = new Empty();
        crispyStartBoard[5][1][0] = new StartPoint();
        crispyStartBoard[5][2][0] = new Wall(Collections.singletonList("right"));


        //line 7:
        crispyStartBoard[6][0][0] = new StartPoint();
        crispyStartBoard[6][1][0] = new Empty();
        crispyStartBoard[6][2][0] = new Empty();


        //line 8:
        crispyStartBoard[7][0][0] = new Empty();
        crispyStartBoard[7][1][0] = new Wall(Collections.singletonList("down"));
        crispyStartBoard[7][2][0] = new Empty();


        //line 9:
        crispyStartBoard[8][0][0] = new Empty();
        crispyStartBoard[8][1][0] = new StartPoint();
        crispyStartBoard[8][2][0] = new Empty();


        //line 10:
        crispyStartBoard[9][0][0] = new Empty(); // Board_Identifiers A
        crispyStartBoard[9][1][0] = new Empty();
        crispyStartBoard[9][2][0] = new Belt("right", 1);

    }

    /**
     * getFields()
     *
     * @return fields
     */
    @Override
    public BoardTile[][][] getFields() {
        return crispyStartBoard;
    }

    /**
     * getBoardType()
     *
     * @return type of map (DizzyHighway)
     */
    @Override
    public String getBoardType() {
        return "CrispyStartBoard";
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
        return crispyStartBoard[y][x][z];
    }
}