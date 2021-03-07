package View;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;

/**
 * handles the adding and removing of cards and its graphics to the game
 * @author Tobias Celik, Hoang An Tran, Simon Wiethase
 */
public class CardsBuilder {

    @FXML
    public GridPane allCards;
    @FXML
    public GridPane programmingCards;
    @FXML
    public GridPane damageCards;

    private final Image againBlueImage;
    private final Image leftTurnBlueImage;
    private final Image moveBackBlueImage;
    private final Image moveOneBlueImage;
    private final Image moveThreeBlueImage;
    private final Image moveTwoBlueImage;
    private final Image powerUpBlueImage;
    private final Image rightTurnBlueImage;
    private final Image uTurnBlueImage;
    private final Image spamCardImage;
    private final Image trojanHorseCardImage;
    private final Image virusCardImage;
    private final Image wormCardImage;
    private final Image playerDeckBackImage;

    Map<Integer, String> allCardsNames = new HashMap<>();
    Map<Integer, String> allProgrammingCardsNames = new HashMap<>();
    Map<Integer, String> damageCardsNames = new HashMap<Integer, String>();

    /**
     * initialises the cards with its own resources
     * @param allCards a 9x1 grid that will be populated with cards
     * @param programmingCards a 5x1 grid that will be populated with the selected cards
     * @param damageCards the additional input used to add damage cards to the grid
     */
    public CardsBuilder(GridPane allCards, GridPane programmingCards, GridPane damageCards){

        this.allCards = allCards;
        this.programmingCards = programmingCards;
        this.damageCards = damageCards;

        for(int i = 0; i< 9;i++){
            setCard(this.allCards, "", i);
            allCardsNames.put(i, "");
        }


        for(int i = 0;i<3;i++){
            setCard(this.damageCards,"",i);
            damageCardsNames.put(i,"");
        }

        InputStream againBlue;
        InputStream leftTurnBlue;
        InputStream moveBackBlue;
        InputStream moveOneBlue;
        InputStream moveThreeBlue;
        InputStream moveTwoBlue;
        InputStream powerUpBlue;
        InputStream rightTurnBlue;
        InputStream uTurnBlue;
        InputStream emptyCard;
        InputStream spamCard;
        InputStream trojanHorseCard;
        InputStream virusCard;
        InputStream wormCard;
        InputStream playerDeckBack;
        againBlue = getClass().getResourceAsStream("/resources/Tiles/Cards/AgainBlue.png");
        leftTurnBlue = getClass().getResourceAsStream("/resources/Tiles/Cards/LeftTurnBlue.png");
        moveBackBlue = getClass().getResourceAsStream("/resources/Tiles/Cards/MoveBackBlue.png");
        moveOneBlue = getClass().getResourceAsStream("/resources/Tiles/Cards/MoveOneBlue.png");
        moveThreeBlue = getClass().getResourceAsStream("/resources/Tiles/Cards/MoveThreeBlue.png");
        moveTwoBlue = getClass().getResourceAsStream("/resources/Tiles/Cards/MoveTwoBlue.png");
        powerUpBlue = getClass().getResourceAsStream("/resources/Tiles/Cards/PowerUpBlue.png");
        rightTurnBlue = getClass().getResourceAsStream("/resources/Tiles/Cards/RightTurnBlue.png");
        uTurnBlue = getClass().getResourceAsStream("/resources/Tiles/Cards/UTurnBlue.png");
        spamCard = getClass().getResourceAsStream("/resources/Tiles/Cards/Spam.png");
        trojanHorseCard = getClass().getResourceAsStream("/resources/Tiles/Cards/Trojan_Horse.png");
        virusCard = getClass().getResourceAsStream("/resources/Tiles/Cards/Virus.png");
        wormCard = getClass().getResourceAsStream("/resources/Tiles/Cards/Worm.png");
        playerDeckBack = getClass().getResourceAsStream("/resources/Tiles/Cards/PlayerDeckBack.png");

        againBlueImage = new Image(againBlue);
        leftTurnBlueImage = new Image(leftTurnBlue);
        moveBackBlueImage = new Image(moveBackBlue);
        moveOneBlueImage = new Image(moveOneBlue);
        moveThreeBlueImage = new Image(moveThreeBlue);
        moveTwoBlueImage = new Image(moveTwoBlue);
        powerUpBlueImage = new Image(powerUpBlue);
        rightTurnBlueImage = new Image(rightTurnBlue);
        uTurnBlueImage = new Image(uTurnBlue);
        spamCardImage = new Image(spamCard);
        trojanHorseCardImage = new Image(trojanHorseCard);
        virusCardImage = new Image(virusCard);
        wormCardImage = new Image(wormCard);
        playerDeckBackImage = new Image(playerDeckBack);
    }

    /**
     * a helper function to create a single instance of an imageview
     * @param image is the image to be converted to an imageview
     * @return ImageView that will be used to be display inside a grid+
     */
    private ImageView toImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80);
        imageView.setFitWidth(60);
        return imageView;
    }

    /**
     * inserts a card addressed by its name into the index of an grid
     * @param gridPane the cards grid to insert into
     * @param card the card name
     * @param index the position to insert into
     */
    public void setCard(GridPane gridPane, String card, int index) {

        switch (card) {
            case "Again" -> gridPane.add(toImageView(againBlueImage), index, 0);
            case "BackUp" -> gridPane.add(toImageView(moveBackBlueImage), index, 0);
            case "MoveI" -> gridPane.add(toImageView(moveOneBlueImage), index, 0);
            case "MoveII" -> gridPane.add(toImageView(moveTwoBlueImage), index, 0);
            case "MoveIII" -> gridPane.add(toImageView(moveThreeBlueImage), index, 0);
            case "PowerUp" -> gridPane.add(toImageView(powerUpBlueImage), index, 0);
            case "TurnLeft" -> gridPane.add(toImageView(leftTurnBlueImage), index, 0);
            case "TurnRight" -> gridPane.add(toImageView(rightTurnBlueImage), index, 0);
            case "UTurn" -> gridPane.add(toImageView(uTurnBlueImage), index, 0);
            case "" -> gridPane.add(toImageView(playerDeckBackImage), index, 0);
            case "SPAM" -> gridPane.add(toImageView(spamCardImage), index, 0);
            case "Trojan Horse" -> gridPane.add(toImageView(trojanHorseCardImage), index, 0);
            case "Virus" -> gridPane.add(toImageView(virusCardImage), index, 0);
            case "Worm" -> gridPane.add(toImageView(wormCardImage), index, 0);
        }
    }
}
