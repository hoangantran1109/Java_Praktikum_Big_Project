package BoardTiles;

import java.util.Arrays;

/**
 * Class DizzyHighway
 * creates the Board DizzyHighway
 * @author whole Team
 */
public class DizzyHighway implements Board {

    private final String boardType = "DizzyHighway";

    private final int columns = 13;
    private final int rows = 10;
    private final int layers = 3;

    private final int checkpoints = 1;


    private final BoardTile[][][] dizzyHighway = new BoardTile[rows][columns][layers];

    /**
     * DizzyHighway()
     * fills the 3-dimensional array with board tiles according to the dizzyHighway map
     */
    public DizzyHighway() {

        //line 1:
        dizzyHighway[0][0][0] = new Empty();
        dizzyHighway[0][1][0] = new Empty();
        dizzyHighway[0][2][0] = new Belt("right", 1);
        dizzyHighway[0][3][0] = new Empty();
        dizzyHighway[0][4][0] = new Belt("down", 2);
        dizzyHighway[0][5][0] = new Belt("down", 2);
        dizzyHighway[0][6][0] = new Empty();
        dizzyHighway[0][7][0] = new Empty();
        dizzyHighway[0][8][0] = new Empty();
        dizzyHighway[0][9][0] = new Empty();
        dizzyHighway[0][10][0] = new Empty();
        dizzyHighway[0][11][0] = new Empty();
        dizzyHighway[0][12][0] = new EnergySpace(1);

        //line 2: Done
        dizzyHighway[1][0][0] = new Empty();
        dizzyHighway[1][1][0] = new StartPoint();
        dizzyHighway[1][2][0] = new Empty();
        dizzyHighway[1][3][0] = new Empty();
        dizzyHighway[1][4][0] = new RotatingBelt(Arrays.asList("down", "left"), 2, true);
        dizzyHighway[1][5][0] = new RotatingBelt(Arrays.asList("left", "down"), 2, true);
        dizzyHighway[1][6][0] = new Belt("left", 2);
        dizzyHighway[1][7][0] = new Belt("left", 2);
        dizzyHighway[1][8][0] = new Belt("left", 2);
        dizzyHighway[1][9][0] = new Belt("left", 2);
        dizzyHighway[1][10][0] = new Belt("left", 2);
        dizzyHighway[1][11][0] = new RotatingBelt(Arrays.asList("left", "up"), 2, true);
        dizzyHighway[1][12][0] = new Belt("left", 2);

        //line 3:
        dizzyHighway[2][0][0] = new Empty();
        dizzyHighway[2][1][0] = new Wall(Arrays.asList("up"));
        dizzyHighway[2][2][0] = new Empty();
        dizzyHighway[2][3][0] = new Empty();
        dizzyHighway[2][4][0] = new Belt("down", 2);
        dizzyHighway[2][5][0] = new EnergySpace(1);
        dizzyHighway[2][6][0] = new Empty();
        dizzyHighway[2][7][0] = new Empty();
        dizzyHighway[2][8][0] = new Empty();
        dizzyHighway[2][9][0] = new Empty();
        dizzyHighway[2][10][0] = new Empty();
        dizzyHighway[2][11][0] = new RotatingBelt(Arrays.asList("up", "left"), 2, true);
        dizzyHighway[2][12][0] = new Belt("left", 2);

        //line 4:
        dizzyHighway[3][0][0] = new StartPoint();
        dizzyHighway[3][1][0] = new Empty();
        dizzyHighway[3][2][0] = new Empty();
        dizzyHighway[3][3][0] = new Empty();
        dizzyHighway[3][4][0] = new Belt("down", 2);
        dizzyHighway[3][5][0] = new Empty();
        dizzyHighway[3][6][0] = new Wall(Arrays.asList("up"));
        dizzyHighway[3][7][0] = new RestartPoint("down"); // 8 Rebooting
        dizzyHighway[3][8][0] = new Wall(Arrays.asList("left"));
        dizzyHighway[3][8][1] = new Laser("right", 1);
        dizzyHighway[3][9][0] = new Wall(Arrays.asList("right"));
        dizzyHighway[3][10][0] = new Empty();
        dizzyHighway[3][11][0] = new Belt("up", 2);
        dizzyHighway[3][12][0] = new CheckPoint(1);


        //line 5:
        dizzyHighway[4][0][0] = new Antenna("right");
        dizzyHighway[4][1][0] = new StartPoint();
        dizzyHighway[4][2][0] = new Wall(Arrays.asList("right"));
        dizzyHighway[4][3][0] = new Empty();
        dizzyHighway[4][4][0] = new Belt("down", 2);
        dizzyHighway[4][5][0] = new Empty();
        dizzyHighway[4][6][0] = new Wall(Arrays.asList("down"));
        dizzyHighway[4][6][1] = new Laser("up", 1);
        dizzyHighway[4][7][0] = new Empty();
        dizzyHighway[4][8][0] = new EnergySpace(1);
        dizzyHighway[4][9][0] = new Empty();
        dizzyHighway[4][10][0] = new Empty();
        dizzyHighway[4][11][0] = new Belt("up", 2);
        dizzyHighway[4][12][0] = new Empty();

        //line 6:
        dizzyHighway[5][0][0] = new Empty();
        dizzyHighway[5][1][0] = new StartPoint();
        dizzyHighway[5][2][0] = new Wall(Arrays.asList("right"));
        dizzyHighway[5][3][0] = new Empty();
        dizzyHighway[5][4][0] = new Belt("down", 2);
        dizzyHighway[5][5][0] = new Empty();
        dizzyHighway[5][6][0] = new Empty();
        dizzyHighway[5][7][0] = new EnergySpace(1);
        dizzyHighway[5][8][0] = new Empty();
        dizzyHighway[5][9][0] = new Wall(Arrays.asList("up"));
        dizzyHighway[5][9][1] = new Laser("down", 1);
        dizzyHighway[5][10][0] = new Empty();
        dizzyHighway[5][11][0] = new Belt("up", 2);
        dizzyHighway[5][12][0] = new Empty();

        //line 7:
        dizzyHighway[6][0][0] = new StartPoint();
        dizzyHighway[6][1][0] = new Empty();
        dizzyHighway[6][2][0] = new Empty();
        dizzyHighway[6][3][0] = new Empty();
        dizzyHighway[6][4][0] = new Belt("down", 2);
        dizzyHighway[6][5][0] = new Empty();
        dizzyHighway[6][6][0] = new Wall(Arrays.asList("left"));
        dizzyHighway[6][7][0] = new Wall(Arrays.asList("right"));
        dizzyHighway[6][7][1] = new Laser("left", 1);
        dizzyHighway[6][8][0] = new Empty();
        dizzyHighway[6][9][0] = new Wall(Arrays.asList("down"));
        dizzyHighway[6][10][0] = new Empty();
        dizzyHighway[6][11][0] = new Belt("up", 2);
        dizzyHighway[6][12][0] = new Empty();

        //line 8:
        dizzyHighway[7][0][0] = new Empty();
        dizzyHighway[7][1][0] = new Wall(Arrays.asList("down"));
        dizzyHighway[7][2][0] = new Empty();
        dizzyHighway[7][3][0] = new Belt("right", 2);
        dizzyHighway[7][4][0] = new RotatingBelt(Arrays.asList("down", "right"), 2, true);
        dizzyHighway[7][5][0] = new Empty();
        dizzyHighway[7][6][0] = new Empty();
        dizzyHighway[7][7][0] = new Empty();
        dizzyHighway[7][8][0] = new Empty();
        dizzyHighway[7][9][0] = new Empty();
        dizzyHighway[7][10][0] = new EnergySpace(1);
        dizzyHighway[7][11][0] = new Belt("up", 2);
        dizzyHighway[7][12][0] = new Empty();

        //line 9:
        dizzyHighway[8][0][0] = new Empty();
        dizzyHighway[8][1][0] = new StartPoint();
        dizzyHighway[8][2][0] = new Empty();
        dizzyHighway[8][3][0] = new Belt("right", 2);
        dizzyHighway[8][4][0] = new RotatingBelt(Arrays.asList("right", "down"), 2, true);
        dizzyHighway[8][5][0] = new Belt("right", 2);
        dizzyHighway[8][6][0] = new Belt("right", 2);
        dizzyHighway[8][7][0] = new Belt("right", 2);
        dizzyHighway[8][8][0] = new Belt("right", 2);
        dizzyHighway[8][9][0] = new Belt("right", 2);
        dizzyHighway[8][10][0] = new RotatingBelt(Arrays.asList("right", "up"), 2, true);
        dizzyHighway[8][11][0] = new RotatingBelt(Arrays.asList("up", "right"), 2, true);
        dizzyHighway[8][12][0] = new Empty();

        //line 10:
        dizzyHighway[9][0][0] = new Empty();
        dizzyHighway[9][1][0] = new Empty();
        dizzyHighway[9][2][0] = new Belt("right", 1);
        dizzyHighway[9][3][0] = new EnergySpace(1);
        dizzyHighway[9][4][0] = new Empty();
        dizzyHighway[9][5][0] = new Empty();
        dizzyHighway[9][6][0] = new Empty();
        dizzyHighway[9][7][0] = new Empty();
        dizzyHighway[9][8][0] = new Empty();
        dizzyHighway[9][9][0] = new Empty();
        dizzyHighway[9][10][0] = new Belt("up", 2);
        dizzyHighway[9][11][0] = new Belt("up", 2);
        dizzyHighway[9][12][0] = new Empty(); // Board_Identifiers 5B
    }

    /**
     * getFields()
     *
     * @return fields
     */
    @Override
    public BoardTile[][][] getFields() {
        return dizzyHighway;
    }

    /**
     * getBoardType()
     *
     * @return type of map (DizzyHighway)
     */
    @Override
    public String getBoardType() {
        return boardType;
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
     * getRows()
     *
     * @return rows
     */
    @Override
    public int getRows() {
        return rows;
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
        return dizzyHighway[y][x][z];
    }

    /**
     * getCheckPoints()
     *
     * @return checkpoints
     */
    public int getCheckpoints() {
        return checkpoints;
    }

}


