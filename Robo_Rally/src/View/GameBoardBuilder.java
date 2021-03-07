package View;

import BoardTiles.*;
import MessageObjects.Field;
import MessageObjects.Map;
import MessageObjects.Message;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class GameBoardBuilder
 * This Class processes the map-information from the Server and builds the Gameboard
 * by placing the Boardtile-Images in the right position
 *
 * @author The whole Team
 */
public class GameBoardBuilder {

    private final static GameBoardBuilder instance = new GameBoardBuilder();
    public SimpleBooleanProperty greenScreen = new SimpleBooleanProperty(false);
    public java.util.Map<Integer, SimpleBooleanProperty> energyCubesVisible = new HashMap<>();
    private GridPane gameBoard;


    public static GameBoardBuilder getInstance() {
        return instance;
    }

    /**
     * Method createCompleteBoard
     * This Method initializes the Gameboard
     * It first creates a ground in the size of the gameboard and places neutral Images.
     * Then it checks the fifth Position which differs in each map to determine which map was selected,
     * so the right startboard is selected.
     * We have to do this, because there is no official message in the Protocol to tell the clients
     * which map was selected
     * In the next step the List of tiles is extracted from the message for later, when single tiles have to be redrawn.
     * When everything is done, the gameboard, message with the List of Maps
     * and a boolean Variable to allow the drawing of Lasers will be handed over to the build Method
     *
     * @param gameBoard   GridPane on which the field will be drawn
     * @param boardWidth  the width of the field
     * @param boardHeight the height of the field
     * @param message     the message which contains all fields
     * @return the message
     */
    public Message createCompleteBoard(GridPane gameBoard, int boardWidth, int boardHeight, Message message) {
        ImageResources.getInstance().Init();
        this.gameBoard = gameBoard;
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                ImageView startPanel = toImageView("neutralImage");
                startPanel.setOpacity(0);
                gameBoard.add(toImageView("neutralImage"), i, j);
            }
        }
        StartBoard startBoard = new CrispyStartBoard();
        if (message.getMessageBody().getMap().get(5).field.get(0).type.equals("Empty")) {
            startBoard = new DizzyStartBoard();
        }

        List<Map> mapFromServer = message.getMessageBody().getMap();
        List<Map> completeMap = addStartBoardToMapFromServer(startBoard, mapFromServer);
        message.getMessageBody().setMap(completeMap);
        build(gameBoard, message, true);
        return message;
    }

    /**
     * This method processes the list of boardtiles in the message and draws the correct image at its position on the
     * GameField
     *
     * @param gameBoard grid, on wich die Images are drawn at
     * @param message   message which contains the List of boardtiles in the right order
     * @param drawLaser boolean variable which denies or allows the drawing of lasers
     */
    public void build(GridPane gameBoard, Message message, boolean drawLaser) {
        for (Map currentMap : message.getMessageBody().getMap()) {
            int x = (currentMap.position) % 13;
            int y = (currentMap.position) / 13;
            List<Field> currentFields = currentMap.field;
            for (Field f : currentFields) {
                if (f.type != null) {
                    f.type = f.type.toLowerCase();
                }
                if (f.orientation != null) {
                    f.orientation = f.orientation.toLowerCase();
                }
            }
            if (message.getMessageBody().getMap().size() == 1) {
                gameBoard.add(toImageView("neutralImage"), x, y);
            }
            if (currentFields.size() == 1 && currentFields.get(0) != null) {
                Field monoLayer = currentFields.get(0);
                switch (monoLayer.type) {
                    case "empty":
                        gameBoard.add(toImageView(("neutralImage")), x, y);
                        break;
                    case "pit":
                        gameBoard.add(toImageView("trapImage"), x, y);
                        break;
                    case "gear":
                        if (monoLayer.orientation.equals("right")) {
                            ImageView gear_clockWise = toImageView("gear_clockwiseImage");
                            gameBoard.add(gear_clockWise, x, y);
                        } else {
                            ImageView gear_counterClockWise = toImageView("gear_counterclockwiseImage");
                            gameBoard.add(gear_counterClockWise, x, y);
                        }
                        break;
                    case "belt":
                        if (monoLayer.orientation.equals("right")) {
                            if (monoLayer.speed == 1) {
                                ImageView conveyor1_eastImage = toImageView("conveyor1_northImage");
                                conveyor1_eastImage.setRotate(90);
                                gameBoard.add(conveyor1_eastImage, x, y);
                            }
                            if (monoLayer.speed == 2) {
                                ImageView conveyor2_downImage = toImageView("conveyor2Image");
                                gameBoard.add(conveyor2_downImage, x, y);
                            }
                        }
                        if (monoLayer.orientation.equals("down")) {
                            if (monoLayer.speed == 2) {
                                ImageView conveyor2_downImage = toImageView("conveyor2Image");
                                conveyor2_downImage.setRotate(90);
                                gameBoard.add(conveyor2_downImage, x, y);
                            }
                        }
                        if (monoLayer.orientation.equals("left")) {
                            if (monoLayer.speed == 1) {
                                ImageView conveyor1_westImage = toImageView("conveyor1_northImage");
                                conveyor1_westImage.setRotate(270);
                                gameBoard.add(conveyor1_westImage, x, y);
                            } else if (monoLayer.speed == 2) {
                                ImageView conveyor2_downImage = toImageView("conveyor2Image");
                                conveyor2_downImage.setRotate(180);
                                gameBoard.add(conveyor2_downImage, x, y);
                            }
                        }
                        if (monoLayer.orientation.equals("up")) {
                            if (monoLayer.speed == 2) {
                                ImageView conveyor2_downImage = toImageView("conveyor2Image");
                                conveyor2_downImage.setRotate(270);
                                gameBoard.add(conveyor2_downImage, x, y);
                            }
                        }
                        break;
                    case "rotatingbelt":
                        String orientation1 = monoLayer.orientations.get(0);
                        String orientation2 = monoLayer.orientations.get(1);
                        ImageView rotatingConveyorBelt;
                        if (orientation1.equals("up") && orientation2.equals("left")
                            || orientation1.equals("right") && orientation2.equals("up")
                            || orientation1.equals("down") && orientation2.equals("right")
                            || orientation1.equals("left") && orientation2.equals("down")) {
                            if (monoLayer.isCrossing) {
                                rotatingConveyorBelt = toImageView("conveyor2_crossingRightImage");
                            } else if (monoLayer.speed == 2) {
                                rotatingConveyorBelt = toImageView("conveyor2_turnRightImage");
                            } else {
                                rotatingConveyorBelt = toImageView("conveyor1_turnRightImage");
                            }
                        } else {
                            if (monoLayer.isCrossing) {
                                rotatingConveyorBelt = toImageView("conveyor2_crossingLeftImage");
                            } else if (monoLayer.speed == 2) {
                                rotatingConveyorBelt = toImageView("conveyor2_turnLeftImage");
                            } else {
                                rotatingConveyorBelt = toImageView("conveyor1_turnLeftImage");
                            }
                        }
                        switch (orientation1) {
                            case "right" -> rotatingConveyorBelt.setRotate(90);
                            case "down" -> rotatingConveyorBelt.setRotate(180);
                            case "left" -> rotatingConveyorBelt.setRotate(270);
                        }
                        gameBoard.add(rotatingConveyorBelt, x, y);
                        break;
                    case "wall":
                        for (String orientation : monoLayer.orientations) {
                            if (orientation.equals("up")) {
                                gameBoard.add(toImageView("wallImage"), x, y);
                            }
                            if (orientation.equals("left")) {
                                ImageView wall_leftImage = toImageView("wallImage");
                                wall_leftImage.setRotate(270);
                                gameBoard.add(wall_leftImage, x, y);
                            }
                            if (orientation.equals("right")) {
                                ImageView wall_rightImage = toImageView("wallImage");
                                wall_rightImage.setRotate(90);
                                gameBoard.add(wall_rightImage, x, y);
                            }
                            if (orientation.equals("down")) {
                                ImageView wall_downImage = toImageView("wallImage");
                                wall_downImage.setRotate(180);
                                gameBoard.add(wall_downImage, x, y);
                            }
                        }
                        break;
                    case "energyspace":
                        ImageView energyCube = toImageView("energyImage");
                        ColorAdjust cube = new ColorAdjust();
                        if (!energyCubesVisible.containsKey(currentMap.position)) {
                            energyCubesVisible.put(currentMap.position, new SimpleBooleanProperty(true));
                        }
                        Blend blush = new Blend(BlendMode.SRC_ATOP, cube, new ColorInput(40, 0, 10, 15, Color.ORANGE)
                        );
                        energyCube.effectProperty().bind(
                                Bindings.when(energyCubesVisible.get(currentMap.position))
                                        .then((Effect) blush)
                                        .otherwise((Effect) null)
                        );
                        gameBoard.add(energyCube, x, y);
                        break;
                    case "startpoint":
                        ImageView startPosition = toImageView("startImage");
                        ColorAdjust redOut = new ColorAdjust();
                        redOut.setSaturation(-1.0);
                        blush = new Blend(
                                BlendMode.MULTIPLY,
                                redOut,
                                new ColorInput(0, 0, 50, 50, Color.GREEN)
                        );
                        startPosition.effectProperty().bind(
                                Bindings.when(greenScreen)
                                        .then((Effect) blush)
                                        .otherwise((Effect) null)
                        );
                        gameBoard.add(startPosition, x, y);
                        Context.getInstance().addPossibleStartPosition(new int[]{x, y});
                        break;
                    case "checkpoint":
                        int checkPointNumber = monoLayer.count;
                        gameBoard.add(toImageView("checkpoint_" + checkPointNumber + "Image"), x, y);
                        break;
                    case "restartpoint":
                        ImageView reboot_tokenImage1 = toImageView("reboot_tokenImage");
                        if (monoLayer.orientation.equals("down")) {
                            reboot_tokenImage1.setRotate(90);
                        }
                        gameBoard.add(reboot_tokenImage1, x, y);
                        break;
                    case "antenna":
                        gameBoard.add(toImageView("antennaImage"), x, y);
                        break;

                }
            } else {
                if (currentFields.contains(new Field("checkpoint"))) {
                    Field currentField = currentFields.get(currentFields.indexOf(new Field("checkpoint")));
                    int checkPointNumber = currentField.count;
                    gameBoard.add(toImageView("checkpoint_" + checkPointNumber + "Image"), x, y);
                } else if (currentFields.contains(new Field("laser"))) {
                    Field currentField = currentFields.get(currentFields.indexOf(new Field("laser")));
                    int count = currentField.count;
                    String orientation = currentField.orientation;
                    ImageView laserImage = null;
                    if (count == 1) {
                        laserImage = toImageView("laser1_startImage");
                    } else if (count == 2) {
                        laserImage = toImageView("laser2_startImage");
                    }
                    assert laserImage != null;
                    if (orientation.equals("down")) {
                        gameBoard.add(laserImage, x, y);
                    }
                    if (orientation.equals("left")) {
                        laserImage.setRotate(90);
                        gameBoard.add(laserImage, x, y);
                    }
                    if (orientation.equals("right")) {
                        laserImage.setRotate(270);
                        gameBoard.add(laserImage, x, y);
                    }
                    if (orientation.equals("up")) {
                        laserImage.setRotate(180);
                        gameBoard.add(laserImage, x, y);
                    }
                } else if (currentFields.contains(new Field("energyspace"))) {
                    ImageView energyCube = toImageView("energy_withWall1Image");
                    ColorAdjust cube = new ColorAdjust();
                    if (!energyCubesVisible.containsKey(currentMap.position)) {
                        energyCubesVisible.put(currentMap.position, new SimpleBooleanProperty(true));
                    }
                    Blend blush = new Blend(BlendMode.SRC_ATOP, cube, new ColorInput(35, 35, 15, 15, Color.ORANGE)
                    );
                    energyCube.effectProperty().bind(
                            Bindings.when(energyCubesVisible.get(currentMap.position))
                                    .then((Effect) blush)
                                    .otherwise((Effect) null)
                    );
                    gameBoard.add(energyCube, x, y);
                }
            }
        }
        if (drawLaser) {
            printLaser(gameBoard, message);
        }
    }

    /**
     * This Method draws Lasers on the field
     * it takes the already written gameBoard and the messages to check, where a Laser must be placed
     *
     * @param gameBoard grid, where all tiles are already drawn on
     * @param message   contains a list of all boardTiles
     */
    private void printLaser(GridPane gameBoard, Message message) {
        int x;
        int y;

        for (Map currentMap : message.getMessageBody().getMap()) {

            x = (currentMap.position) % 13;
            y = (currentMap.position - 1) / 13;

            for (Field currentField : currentMap.field) {
                if (currentField != null) {
                    if (currentField.type.equals("laser")) {
                        boolean stopLaser = false;
                        int x2;
                        int y2;

                        int stopLaserErrorBreak = 0;

                        while (!stopLaser || stopLaserErrorBreak < 13) {

                            stopLaserErrorBreak++;

                            stopLaser = false;

                            for (Map currentMapContinued : message.getMessageBody().getMap()) {

                                x2 = (currentMapContinued.position) % 13;
                                y2 = (currentMapContinued.position - 1) / 13;

                                if (currentField.orientation.equals("down") && y2 == y + 1 && x2 == x) {
                                    stopLaser = checkForLaserStopReason(currentMapContinued);
                                    if (stopLaser) break;

                                    if (checkForWall(currentMapContinued)) {
                                        drawWallLaser(gameBoard, currentMapContinued.position, currentField.orientation);
                                        stopLaser = true;
                                        break;
                                    } else {
                                        drawNeutralLaser(gameBoard, currentMapContinued.position, currentField.orientation);
                                        y++;
                                    }
                                } else if (currentField.orientation.equals("left") && y2 == y && x2 == x - 1) {

                                    stopLaser = checkForLaserStopReason(currentMapContinued);
                                    if (stopLaser) break;

                                    if (checkForWall(currentMapContinued)) {
                                        drawWallLaser(gameBoard, currentMapContinued.position, currentField.orientation);
                                        stopLaser = true;
                                        break;
                                    } else {
                                        drawNeutralLaser(gameBoard, currentMapContinued.position, currentField.orientation);
                                        x--;
                                    }
                                } else if (currentField.orientation.equals("right") && y2 == y && x2 == x + 1) {

                                    stopLaser = checkForLaserStopReason(currentMapContinued);
                                    if (stopLaser) break;

                                    if (checkForWall(currentMapContinued)) {
                                        drawWallLaser(gameBoard, currentMapContinued.position, currentField.orientation);
                                        stopLaser = true;
                                        break;
                                    } else {
                                        drawNeutralLaser(gameBoard, currentMapContinued.position, currentField.orientation);
                                        x++;
                                    }
                                } else if (currentField.orientation.equals("up") && y2 == y - 1 && x2 == x) {

                                    stopLaser = checkForLaserStopReason(currentMapContinued);
                                    if (stopLaser) break;

                                    if (checkForWall(currentMapContinued)) {
                                        drawWallLaser(gameBoard, currentMapContinued.position, currentField.orientation);
                                        stopLaser = true;
                                        break;
                                    } else {
                                        drawNeutralLaser(gameBoard, currentMapContinued.position, currentField.orientation);
                                        y--;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This method checks if a tile marks the end of a laser. The current Tiles to do so are Walls and Checkpoints.
     * If true, the drawLaser Method stops drawing the laser.
     * If false, it continues to draw
     *
     * @param currentMapContinued boardTile which has to be checked
     * @return tells the drawLaser function whether to continue or stop the drawing of the laser
     */
    private boolean checkForLaserStopReason(Map currentMapContinued) {
        for (Field field : currentMapContinued.field) {
            if (field.type.equals("laser")
                || field.type.equals("checkpoint_1Image")
                || field.type.equals("checkpoint_2Image")
                || field.type.equals("checkpoint_3Image")
                || field.type.equals("checkpoint_4Image")
                || Context.getInstance().getNeutralLasersOnMap().containsKey(currentMapContinued.position)
                || Context.getInstance().getEndLasersOnMap().containsKey(currentMapContinued.position)
            ) {
                return true;
            }
        }
        return false;
    }

    /**
     * This Method checks, if a field is a Wall, it is used in the drawLaser Function
     *
     * @param currentMapContinued the boardTile which must be checked
     * @return true, if it is a Wall, false if not
     */
    private boolean checkForWall(Map currentMapContinued) {
        for (Field field : currentMapContinued.field) {
            if (field.type.equals("wall")) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method places the predefined Image of a laser on a neutral field on the correct position
     * in the gameBoard
     *
     * @param gameBoard   grid of the gameBoard
     * @param position    position of the image
     * @param orientation orientation if the image
     */
    public void drawNeutralLaser(GridPane gameBoard, int position, String orientation) {
        ImageView laserNeutralImage = toImageView("laser1_neutralImage");
        int x = (position) % 13;
        int y = (position - 1) / 13;

        switch (orientation) {
            case "left" -> laserNeutralImage.setRotate(90);
            case "right" -> laserNeutralImage.setRotate(270);
            case "up" -> laserNeutralImage.setRotate(180);
        }
        Platform.runLater(() -> gameBoard.add(laserNeutralImage, x, y));
        Context.getInstance().putToNeutralLasersOnMap(position, orientation);
    }

    /**
     * This method places the predefined Image of the conjunction of a laser and a wall in the correct position
     * in the gameBoard
     *
     * @param gameBoard   grid of the gameBoard
     * @param position    position of the image
     * @param orientation orientation if the image
     */
    public void drawWallLaser(GridPane gameBoard, int position, String orientation) {
        ImageView laserEndImage = toImageView("laser1_endImage");
        int x = (position) % 13;
        int y = (position - 1) / 13;
        switch (orientation) {
            case "left" -> laserEndImage.setRotate(90);
            case "right" -> laserEndImage.setRotate(270);
            case "up" -> laserEndImage.setRotate(180);
        }
        Platform.runLater(() -> gameBoard.add(laserEndImage, x, y));
        Context.getInstance().putToEndLasersOnMap(position, orientation);
    }

    /**
     * This Method retrieves the Image from an internal Image Database, Converts it to an ImageView object,
     * and fits its boundaries to the current height and width of the GameWindow
     *
     * @param imageName name of the Image
     * @return Imageview Object of the requested Image
     */
    private ImageView toImageView(String imageName) {
        ImageView imageView = new ImageView(ImageResources.getInstance().getImage(imageName));
        imageView.fitWidthProperty().bind(gameBoard.widthProperty().divide(13));
        imageView.fitHeightProperty().bind(gameBoard.heightProperty().divide(10));
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * This Method joins the Map Information of the Game Field (Server sends this Information)
     * and the StartBoard (locally stored in the Client)
     *
     * @param startBoard    the locally Stored StartBoard
     * @param mapFromServer the GameBoard the Server sends to the Clients
     * @return one List containing the Information of both boards
     */
    private List<Map> addStartBoardToMapFromServer(StartBoard startBoard, List<Map> mapFromServer) {
        int position = 0;
        for (int y = 0; y < startBoard.getFields().length; y++) {
            for (int x = 0; x < startBoard.getFields()[y].length; x++) {
                Map mapFromStartBoardTile = new Map();
                List<Field> fields = new ArrayList<>();
                Field fieldFromStartBoardTile = new Field(startBoard.getFields()[y][x][0].getClass().getSimpleName());
                switch (fieldFromStartBoardTile.type) {
                    case "Belt" -> {
                        Belt belt = (Belt) startBoard.getFields()[y][x][0];
                        fieldFromStartBoardTile.orientation = belt.getOrientation();
                        fieldFromStartBoardTile.speed = belt.getSpeed();
                    }
                    case "Wall" -> {
                        Wall wall = (Wall) startBoard.getFields()[y][x][0];
                        fieldFromStartBoardTile.orientations = wall.getOrientations();
                    }
                    case "StartPosition" -> {
                        StartPoint startPosition = (StartPoint) startBoard.getFields()[y][x][0];
                        fieldFromStartBoardTile.type = startPosition.getType();
                    }
                    case "Antenna" -> {
                        Antenna antenna = (Antenna) startBoard.getFields()[y][x][0];
                        fieldFromStartBoardTile.orientation = antenna.getOrientation();
                    }
                    case "RestartPoint" -> {
                        RestartPoint restartPoint = (RestartPoint) startBoard.getFields()[y][x][0];
                        fieldFromStartBoardTile.orientation = restartPoint.getOrientation();
                    }
                }
                fields.add(fieldFromStartBoardTile);
                mapFromStartBoardTile.position = position;
                mapFromStartBoardTile.field = fields;
                mapFromServer.add(mapFromStartBoardTile);
                if (position % 13 == 2) {
                    position += 11;
                } else {
                    position++;
                }
            }
        }
        return mapFromServer;
    }
}