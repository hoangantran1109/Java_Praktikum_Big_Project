package BoardTiles;

import java.util.Arrays;

/**
 * Class ExtraCrispy
 * creates the Board ExtraCrispy
 *
 * @author Alina Klessinger
 */
public class ExtraCrispy implements Board {

    private final String boardType = "ExtraCrispy";

    private final int columns = 13;
    private final int rows = 10;
    private final int layers = 3;

    private final int checkpoints = 4;

    private final BoardTile[][][] extraCrispy = new BoardTile[rows][columns][layers];

    /**
     * ExtraCrispy()
     * fills the 3-dimensional array with board tiles according to the ExtraCrispy map
     */
    public ExtraCrispy() {

        //line 1:
        extraCrispy[0][0][0] = new RestartPoint("right");
        extraCrispy[0][1][0] = new Empty();
        extraCrispy[0][2][0] = new Belt("right", 1);


        extraCrispy[0][3][0] = new Empty(); // Board_Identifiers 4A
        extraCrispy[0][4][0] = new Belt("down", 2);
        extraCrispy[0][5][0] = new Empty();
        extraCrispy[0][6][0] = new Empty();
        extraCrispy[0][7][0] = new Empty();

        extraCrispy[0][8][0] = new EnergySpace(1);
        extraCrispy[0][9][0] = new Wall(Arrays.asList("left"));
        extraCrispy[0][9][1] = new Laser("right", 1);
        extraCrispy[0][10][0] = new Empty();
        extraCrispy[0][11][0] = new Wall(Arrays.asList("right"));
        extraCrispy[0][11][1] = new Laser("left", 1);
        extraCrispy[0][12][0] = new Empty();

        //line 2: Done
        extraCrispy[1][0][0] = new Empty();
        extraCrispy[1][1][0] = new StartPoint();
        extraCrispy[1][2][0] = new Empty();


        extraCrispy[1][3][0] = new Empty();
        extraCrispy[1][4][0] = new Belt("down", 2);
        extraCrispy[1][5][0] = new Empty();
        extraCrispy[1][6][0] = new RotatingBelt(Arrays.asList("down", "left"), 1, false);
        extraCrispy[1][7][0] = new Belt("left", 1);

        extraCrispy[1][8][0] = new Empty();
        extraCrispy[1][9][0] = new Empty();
        extraCrispy[1][10][0] = new Empty();
        extraCrispy[1][11][0] = new Belt("down", 2);
        extraCrispy[1][12][0] = new Empty();

        //line 3:
        extraCrispy[2][0][0] = new Empty();
        extraCrispy[2][1][0] = new Wall(Arrays.asList("up"));
        extraCrispy[2][2][0] = new Empty();


        extraCrispy[2][3][0] = new Empty();
        extraCrispy[2][4][0] = new Belt("down", 2);
        extraCrispy[2][5][0] = new Wall(Arrays.asList("up"));
        extraCrispy[2][5][1] = new Laser("down", 1);
        extraCrispy[2][5][2] = new CheckPoint(4);
        extraCrispy[2][6][0] = new Pit();
        extraCrispy[2][7][0] = new Empty();

        extraCrispy[2][8][0] = new Empty();
        extraCrispy[2][9][0] = new Pit();
        extraCrispy[2][10][0] = new Wall(Arrays.asList("up"));
        extraCrispy[2][10][1] = new Laser("down", 1);
        extraCrispy[2][10][2] = new CheckPoint(1);
        extraCrispy[2][11][0] = new Belt("down", 2);
        extraCrispy[2][12][0] = new Empty();

        //line 4:
        extraCrispy[3][0][0] = new StartPoint();
        extraCrispy[3][1][0] = new Empty();
        extraCrispy[3][2][0] = new Empty();


        extraCrispy[3][3][0] = new Empty();
        extraCrispy[3][4][0] = new RotatingBelt(Arrays.asList("right", "down"), 2, false);
        extraCrispy[3][5][0] = new Belt("right", 2);
        extraCrispy[3][6][0] = new Pit();
        extraCrispy[3][7][0] = new Wall(Arrays.asList("left"));
        extraCrispy[3][7][1] = new Laser("right", 1);

        extraCrispy[3][8][0] = new Wall(Arrays.asList("right"));
        extraCrispy[3][8][1] = new Laser("left", 1);
        extraCrispy[3][9][0] = new Pit();
        extraCrispy[3][10][0] = new Belt("left", 2);
        extraCrispy[3][11][0] = new RotatingBelt(Arrays.asList("left", "down"), 2, false);
        extraCrispy[3][12][0] = new Empty();

        //line 5:
        extraCrispy[4][0][0] = new Antenna("right");
        extraCrispy[4][1][0] = new StartPoint();
        extraCrispy[4][2][0] = new Wall(Arrays.asList("right"));


        extraCrispy[4][3][0] = new Wall(Arrays.asList("up"));
        extraCrispy[4][3][1] = new EnergySpace(1);
        extraCrispy[4][4][0] = new Empty();
        extraCrispy[4][5][0] = new Empty();
        extraCrispy[4][6][0] = new Gear("right");
        extraCrispy[4][7][0] = new Empty();

        extraCrispy[4][8][0] = new Empty();
        extraCrispy[4][9][0] = new Wall(Arrays.asList("right"));
        extraCrispy[4][10][0] = new Empty();
        extraCrispy[4][11][0] = new EnergySpace(1);
        extraCrispy[4][12][0] = new Wall(Arrays.asList("up"));


        //line 6:
        extraCrispy[5][0][0] = new Empty();
        extraCrispy[5][1][0] = new StartPoint();
        extraCrispy[5][2][0] = new Wall(Arrays.asList("right"));


        extraCrispy[5][3][0] = new Wall(Arrays.asList("down"));
        extraCrispy[5][4][0] = new Empty();
        extraCrispy[5][5][0] = new Empty();
        extraCrispy[5][6][0] = new Wall(Arrays.asList("left"));
        extraCrispy[5][7][0] = new EnergySpace(1);

        extraCrispy[5][8][0] = new Empty();
        extraCrispy[5][9][0] = new Gear("left");
        extraCrispy[5][10][0] = new Empty();
        extraCrispy[5][11][0] = new Empty();
        extraCrispy[5][12][0] = new Wall(Arrays.asList("down"));

        //line 7:
        extraCrispy[6][0][0] = new StartPoint();
        extraCrispy[6][1][0] = new Empty();
        extraCrispy[6][2][0] = new Empty();


        extraCrispy[6][3][0] = new Empty();
        extraCrispy[6][4][0] = new RotatingBelt(Arrays.asList("right", "up"), 2, false);
        extraCrispy[6][5][0] = new Belt("right", 2);
        extraCrispy[6][6][0] = new Pit();
        extraCrispy[6][7][0] = new Wall(Arrays.asList("left"));
        extraCrispy[6][7][1] = new Laser("right", 1);

        extraCrispy[6][8][0] = new Wall(Arrays.asList("right"));
        extraCrispy[6][8][1] = new Laser("left", 1);
        extraCrispy[6][9][0] = new Pit();
        extraCrispy[6][10][0] = new Belt("left", 2);
        extraCrispy[6][11][0] = new RotatingBelt(Arrays.asList("left", "up"), 2, false);
        extraCrispy[6][12][0] = new Empty();

        //line 8:
        extraCrispy[7][0][0] = new Empty();
        extraCrispy[7][1][0] = new Wall(Arrays.asList("down"));
        extraCrispy[7][2][0] = new Empty();


        extraCrispy[7][3][0] = new Empty();
        extraCrispy[7][4][0] = new Belt("up", 2);
        extraCrispy[7][5][0] = new Wall(Arrays.asList("down"));
        extraCrispy[7][5][1] = new Laser("up", 1);
        extraCrispy[7][5][2] = new CheckPoint(2);
        extraCrispy[7][6][0] = new Pit();
        extraCrispy[7][7][0] = new Empty();

        extraCrispy[7][8][0] = new Empty();
        extraCrispy[7][9][0] = new Pit();
        extraCrispy[7][10][0] = new Wall(Arrays.asList("down"));
        extraCrispy[7][10][1] = new Laser("up", 1);
        extraCrispy[7][10][2] = new CheckPoint(3);
        extraCrispy[7][11][0] = new Belt("up", 2);
        extraCrispy[7][12][0] = new Empty();

        //line 9:
        extraCrispy[8][0][0] = new Empty();
        extraCrispy[8][1][0] = new StartPoint();
        extraCrispy[8][2][0] = new Empty();


        extraCrispy[8][3][0] = new Empty();
        extraCrispy[8][4][0] = new Belt("up", 2);
        extraCrispy[8][5][0] = new Empty();
        extraCrispy[8][6][0] = new Empty();
        extraCrispy[8][7][0] = new Empty();

        extraCrispy[8][8][0] = new Belt("right", 1);
        extraCrispy[8][9][0] = new RotatingBelt(Arrays.asList("up", "right"), 1, false);
        extraCrispy[8][10][0] = new Empty();
        extraCrispy[8][11][0] = new Belt("up", 2);
        extraCrispy[8][12][0] = new Empty();

        //line 10:
        extraCrispy[9][0][0] = new Empty(); // Board_Identifiers A
        extraCrispy[9][1][0] = new Empty();
        extraCrispy[9][2][0] = new Belt("right", 1);

        extraCrispy[9][3][0] = new EnergySpace(1);
        extraCrispy[9][4][0] = new Wall(Arrays.asList("left"));
        extraCrispy[9][4][1] = new Laser("right", 1);
        extraCrispy[9][5][0] = new Empty();
        extraCrispy[9][6][0] = new Wall(Arrays.asList("right"));
        extraCrispy[9][6][1] = new Laser("left", 1);
        extraCrispy[9][7][0] = new Empty();

        extraCrispy[9][8][0] = new Empty();
        extraCrispy[9][9][0] = new Empty();
        extraCrispy[9][10][0] = new Empty();
        extraCrispy[9][11][0] = new Belt("up", 2);
        extraCrispy[9][12][0] = new Empty();
    }


    @Override
    public BoardTile[][][] getFields() {
        return extraCrispy;
    }

    @Override
    public String getBoardType() {
        return boardType;
    }

    @Override
    public int getCheckpoints() {
        return checkpoints;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public BoardTile getField(int y, int x, int z) {
        return extraCrispy[y][x][z];
    }
}
