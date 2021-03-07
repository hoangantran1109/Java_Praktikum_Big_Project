package View;

import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This Singleton Class is some sort of Database Manager for Images of all fieldtypes
 * It reads the Images stored in the resources folder and puts them in a Hashmap.
 *
 * @author Tobias Celik, Hoang An Tran, Simon Wiethase
 */
public class ImageResources {

    private final static ImageResources instance = new ImageResources();
    private Map<String, Image> imageDB;

    public static ImageResources getInstance() {
        return instance;
    }

    /**
     * this Method initializes the Database:
     * 1. Step: InputStream for every type of boardTiles
     * 2. Step: try to read the Files and Store them as an Image
     * 3. Step: put the Image and its Unique Name in a Hashmap for an easy retrieval by other classes
     */
    public void Init() {
        imageDB = new HashMap<>();
        InputStream neutral;
        InputStream start;
        InputStream trap;
        InputStream wall;

        InputStream identifier_board_1A;
        InputStream identifier_board_1B;
        InputStream identifier_board_2A;
        InputStream identifier_board_2B;
        InputStream identifier_board_3A;
        InputStream identifier_board_3B;
        InputStream identifier_board_4B;
        InputStream identifier_board_5A;
        InputStream identifier_board_5B;
        InputStream identifier_board_6A;
        InputStream identifier_board_6B;
        InputStream identifier_board_A;

        InputStream conveyor1_north;
        InputStream conveyor2_east;
        InputStream conveyor1_turnLeft;
        InputStream conveyor1_turnRight;
        InputStream conveyor2;
        InputStream conveyor2_crossingLeft;
        InputStream conveyor2_crossingRight;
        InputStream conveyor2_turnRight;
        InputStream conveyor2_turnLeft;
        InputStream conveyor2_turnRight_withLaser_1;
        InputStream conveyor2_withLaser;
        InputStream conveyor2_withLaser2;

        InputStream energy;
        InputStream energy_withWall1;
        InputStream energy_withWall2;

        InputStream gear_clockwise;
        InputStream gear_counterclockwise;

        InputStream push_24;
        InputStream push_135;

        InputStream laser1_end;
        InputStream laser1_neutral;
        InputStream laser1_start;
        InputStream laser2_end;
        InputStream laser2_start;
        InputStream laser2_withWall;
        InputStream laser3_startEnd;

        InputStream checkpoint_1;
        InputStream checkpoint_2;
        InputStream checkpoint_3;
        InputStream checkpoint_4;

        InputStream reboot_token;
        InputStream antenna;
        neutral = getClass().getResourceAsStream("/resources/Tiles/Neutral.png");
        start = getClass().getResourceAsStream("/resources/Tiles/Start.png");
        trap = getClass().getResourceAsStream("/resources/Tiles/trap.png");
        wall = getClass().getResourceAsStream("/resources/Tiles/Wall.png");

        identifier_board_1A = getClass().getResourceAsStream(   "/resources/Tiles/Board_Identifiers/Identifier_Board_1A.png");
        identifier_board_1B = getClass().getResourceAsStream("/resources/Tiles/Board_Identifiers/Identifier_Board_1B.png");
        identifier_board_2A = getClass().getResourceAsStream("/resources/Tiles/Board_Identifiers/Identifier_Board_2A.png");
        identifier_board_2B = getClass().getResourceAsStream("/resources/Tiles/Board_Identifiers/Identifier_Board_2B.png");
        identifier_board_3A = getClass().getResourceAsStream("/resources/Tiles/Board_Identifiers/Identifier_Board_3A.png");
        identifier_board_3B = getClass().getResourceAsStream("/resources/Tiles/Board_Identifiers/Identifier_Board_3B.png");
        identifier_board_4B = getClass().getResourceAsStream("/resources/Tiles/Board_Identifiers/Identifier_Board_4B.png");
        identifier_board_5A = getClass().getResourceAsStream("/resources/Tiles/Board_Identifiers/Identifier_Board_5A.png");
        identifier_board_5B = getClass().getResourceAsStream("/resources/Tiles/Board_Identifiers/Identifier_Board_5B.png");
        identifier_board_6A = getClass().getResourceAsStream("/resources/Tiles/Board_Identifiers/Identifier_Board_6A.png");
        identifier_board_6B = getClass().getResourceAsStream("/resources/Tiles/Board_Identifiers/Identifier_Board_6B.png");
        identifier_board_A = getClass().getResourceAsStream ("/resources/Tiles/Board_Identifiers/Identifier_Board_A.png");

        antenna = getClass().getResourceAsStream("/resources/Tiles/Antenna.png");

        conveyor1_north = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor1_north.png");
        conveyor2_east = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor2.png");
        conveyor1_turnLeft = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor1_turnLeft.png");
        conveyor1_turnRight = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor1_turnRight.png");
        conveyor2 = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor2.png");
        conveyor2_crossingLeft = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor2_crossingLeft.png");
        conveyor2_crossingRight = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor2_crossingRight.png");
        conveyor2_turnRight = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor2_turnRight.png");
        conveyor2_turnRight_withLaser_1 = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor2_turnRight_withLaser_1.png");
        conveyor2_turnLeft = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor2_turnLeft.png");
        conveyor2_withLaser = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor2_withLaser.png");
        conveyor2_withLaser2 = getClass().getResourceAsStream("/resources/Tiles/Conveyors/Conveyor2_withLaser2.png");

        energy = getClass().getResourceAsStream("/resources/Tiles/EnergyGroup/Energy.png");
        energy_withWall1 = getClass().getResourceAsStream("/resources/Tiles/EnergyGroup/Energy_withWall1.png");
        energy_withWall2 = getClass().getResourceAsStream("/resources/Tiles/EnergyGroup/Energy_withWall2.png");

        gear_clockwise = getClass().getResourceAsStream("/resources/Tiles/Gears&Push/Gear_clockwise.png");
        gear_counterclockwise = getClass().getResourceAsStream("/resources/Tiles/Gears&Push/Gear_counterclockwise.png");
        push_24 = getClass().getResourceAsStream("/resources/Tiles/Gears&Push/Push_24.png");
        push_135 = getClass().getResourceAsStream("/resources/Tiles/Gears&Push/Push_135.png");

        laser1_end = getClass().getResourceAsStream("/resources/Tiles/Laser/Laser1_end.png");
        laser1_neutral = getClass().getResourceAsStream("/resources/Tiles/Laser/Laser1_neutral.png");
        laser1_start = getClass().getResourceAsStream("/resources/Tiles/Laser/Laser1_start.png");
        laser2_end = getClass().getResourceAsStream("/resources/Tiles/Laser/Laser2_end.png");
        laser2_start = getClass().getResourceAsStream("/resources/Tiles/Laser/Laser2_start.png");
        laser2_withWall = getClass().getResourceAsStream("/resources/Tiles/Laser/Laser2_withWall.png");
        laser3_startEnd = getClass().getResourceAsStream("/resources/Tiles/Laser/Laser3_startEnd.png");

        checkpoint_1 = getClass().getResourceAsStream( "/resources/Tiles/Checkpoint_1.png");
        checkpoint_2 = getClass().getResourceAsStream( "/resources/Tiles/Checkpoint_2.png");
        checkpoint_3 = getClass().getResourceAsStream( "/resources/Tiles/Checkpoint_3.png");
        checkpoint_4 = getClass().getResourceAsStream( "/resources/Tiles/Checkpoint_4.png");


        reboot_token = getClass().getResourceAsStream(   "/resources/Tiles/RestartToken.png");

        Image neutralImage = new Image(Objects.requireNonNull(neutral));
        Image startImage = new Image(Objects.requireNonNull(start));
        Image trapImage = new Image(Objects.requireNonNull(trap));
        Image wallImage = new Image(Objects.requireNonNull(wall));

        Image identifier_board_1AImage = new Image(Objects.requireNonNull(identifier_board_1A));
        Image identifier_board_1BImage = new Image(Objects.requireNonNull(identifier_board_1B));
        Image identifier_board_2AImage = new Image(Objects.requireNonNull(identifier_board_2A));
        Image identifier_board_2BImage = new Image(Objects.requireNonNull(identifier_board_2B));
        Image identifier_board_3AImage = new Image(Objects.requireNonNull(identifier_board_3A));
        Image identifier_board_3BImage = new Image(Objects.requireNonNull(identifier_board_3B));
        Image identifier_board_4BImage = new Image(Objects.requireNonNull(identifier_board_4B));
        Image identifier_board_5AImage = new Image(Objects.requireNonNull(identifier_board_5A));
        Image identifier_board_5BImage = new Image(Objects.requireNonNull(identifier_board_5B));
        Image identifier_board_6AImage = new Image(Objects.requireNonNull(identifier_board_6A));
        Image identifier_board_6BImage = new Image(Objects.requireNonNull(identifier_board_6B));
        Image identifier_board_AImage = new Image(Objects.requireNonNull(identifier_board_A));


        Image conveyor1_northImage = new Image(Objects.requireNonNull(conveyor1_north));
        Image conveyor2_eastImage = new Image(Objects.requireNonNull(conveyor2_east));
        Image conveyor1_turnLeftImage = new Image(Objects.requireNonNull(conveyor1_turnLeft));
        Image conveyor1_turnRightImage = new Image(Objects.requireNonNull(conveyor1_turnRight));
        Image conveyor2Image = new Image(Objects.requireNonNull(conveyor2));
        Image conveyor2_crossingLeftImage = new Image(Objects.requireNonNull(conveyor2_crossingLeft));
        Image conveyor2_crossingRightImage = new Image(Objects.requireNonNull(conveyor2_crossingRight));

        Image conveyor2_turnRightImage = new Image(Objects.requireNonNull(conveyor2_turnRight));
        Image conveyor2_turnLeftImage = new Image(Objects.requireNonNull(conveyor2_turnLeft));
        Image conveyor2_turnRight_withLaser_1Image = new Image(Objects.requireNonNull(conveyor2_turnRight_withLaser_1));
        Image conveyor2_withLaserImage = new Image(Objects.requireNonNull(conveyor2_withLaser));
        Image conveyor2_withLaser2Image = new Image(Objects.requireNonNull(conveyor2_withLaser2));

        Image energyImage = new Image(Objects.requireNonNull(energy));
        Image energy_withWall1Image = new Image(Objects.requireNonNull(energy_withWall1));
        Image energy_withWall2Image = new Image(Objects.requireNonNull(energy_withWall2));

        Image gear_clockwiseImage = new Image(Objects.requireNonNull(gear_clockwise));
        Image gear_counterclockwiseImage = new Image(Objects.requireNonNull(gear_counterclockwise));
        Image push_24Image = new Image(Objects.requireNonNull(push_24));
        Image push_135Image = new Image(Objects.requireNonNull(push_135));

        Image laser1_endImage = new Image(Objects.requireNonNull(laser1_end));
        Image laser1_neutralImage = new Image(Objects.requireNonNull(laser1_neutral));
        Image laser1_startImage = new Image(Objects.requireNonNull(laser1_start));
        Image laser2_endImage = new Image(Objects.requireNonNull(laser2_end));
        Image laser2_startImage = new Image(Objects.requireNonNull(laser2_start));
        Image laser2_withWallImage = new Image(Objects.requireNonNull(laser2_withWall));
        Image laser3_startEndImage = new Image(Objects.requireNonNull(laser3_startEnd));

        Image checkpoint_1Image = new Image(Objects.requireNonNull(checkpoint_1));
        Image checkpoint_2Image = new Image(Objects.requireNonNull(checkpoint_2));
        Image checkpoint_3Image = new Image(Objects.requireNonNull(checkpoint_3));
        Image checkpoint_4Image = new Image(Objects.requireNonNull(checkpoint_4));
        Image reboot_tokenImage = new Image(Objects.requireNonNull(reboot_token));

        Image antennaImage = new Image(Objects.requireNonNull(antenna));

        imageDB.put("neutralImage", neutralImage);
        imageDB.put("startImage", startImage);
        imageDB.put("trapImage", trapImage);
        imageDB.put("wallImage", wallImage);
        imageDB.put("identifier_board_1AImage", identifier_board_1AImage);
        imageDB.put("identifier_board_1BImage", identifier_board_1BImage);
        imageDB.put("identifier_board_2AImage", identifier_board_2AImage);
        imageDB.put("identifier_board_2BImage", identifier_board_2BImage);
        imageDB.put("identifier_board_3AImage", identifier_board_3AImage);
        imageDB.put("identifier_board_3BImage", identifier_board_3BImage);
        imageDB.put("identifier_board_4BImage", identifier_board_4BImage);
        imageDB.put("identifier_board_5AImage", identifier_board_5AImage);
        imageDB.put("identifier_board_5BImage", identifier_board_5BImage);
        imageDB.put("identifier_board_6AImage", identifier_board_6AImage);
        imageDB.put("identifier_board_6BImage", identifier_board_6BImage);
        imageDB.put("identifier_board_AImage", identifier_board_AImage);
        imageDB.put("conveyor1_northImage", conveyor1_northImage);
        imageDB.put("conveyor2_eastImage", conveyor2_eastImage);
        imageDB.put("conveyor1_turnLeftImage", conveyor1_turnLeftImage);
        imageDB.put("conveyor1_turnRightImage", conveyor1_turnRightImage);
        imageDB.put("conveyor2Image", conveyor2Image);
        imageDB.put("conveyor2_crossingLeftImage", conveyor2_crossingLeftImage);
        imageDB.put("conveyor2_crossingRightImage", conveyor2_crossingRightImage);
        imageDB.put("conveyor2_turnRightImage", conveyor2_turnRightImage);
        imageDB.put("conveyor2_turnLeftImage", conveyor2_turnLeftImage);
        imageDB.put("conveyor2_turnRight_withLaser_1Image", conveyor2_turnRight_withLaser_1Image);
        imageDB.put("conveyor2_withLaserImage", conveyor2_withLaserImage);
        imageDB.put("conveyor2_withLaser2Image", conveyor2_withLaser2Image);
        imageDB.put("energyImage", energyImage);
        imageDB.put("energy_withWall1Image", energy_withWall1Image);
        imageDB.put("energy_withWall2Image", energy_withWall2Image);
        imageDB.put("gear_clockwiseImage", gear_clockwiseImage);
        imageDB.put("gear_counterclockwiseImage", gear_counterclockwiseImage);
        imageDB.put("push_24Image", push_24Image);
        imageDB.put("push_135Image", push_135Image);
        imageDB.put("laser1_endImage", laser1_endImage);
        imageDB.put("laser1_neutralImage", laser1_neutralImage);
        imageDB.put("laser1_startImage", laser1_startImage);
        imageDB.put("laser2_endImage", laser2_endImage);
        imageDB.put("laser2_startImage", laser2_startImage);
        imageDB.put("laser2_withWallImage", laser2_withWallImage);
        imageDB.put("laser3_startEndImage", laser3_startEndImage);
        imageDB.put("checkpoint_1Image", checkpoint_1Image);
        imageDB.put("checkpoint_2Image", checkpoint_2Image);
        imageDB.put("checkpoint_3Image", checkpoint_3Image);
        imageDB.put("checkpoint_4Image", checkpoint_4Image);
        imageDB.put("reboot_tokenImage", reboot_tokenImage);
        imageDB.put("antennaImage", antennaImage);
    }

    public Image getImage(String name) {
        return imageDB.get(name);
    }
}