package View;

import javafx.scene.image.Image;
import java.io.InputStream;

/**
 * Class RoboImage
 * This class is created to take the images of robot
 * @author Hoang An Tran, Simon Wiethase, Tobias Celik
 */
public class RoboImage {
    private final Image hammerBotImage;
    private final Image hulkX90Image;
    private final Image smashBotImage;
    private final Image spinBotImage;
    private final Image twonkyImage;
    private final Image zoomBotImage;

    /**
     * RoboImage()
     * Instantiates a new Robo image.
     */
    public RoboImage() {
        InputStream hammerBot;
        InputStream hulkX90;
        InputStream smashBot;
        InputStream spinBot;
        InputStream twonky;
        InputStream zoomBot;

        hammerBot = getClass().getResourceAsStream("/resources/Robots/HammerBot.png");
        hulkX90 = getClass().getResourceAsStream("/resources/Robots/HulkX90.png");
        smashBot = getClass().getResourceAsStream("/resources/Robots/SmashBot.png");
        spinBot = getClass().getResourceAsStream("/resources/Robots/Spinbot.png");
        twonky = getClass().getResourceAsStream("/resources/Robots/Twonky.png");
        zoomBot = getClass().getResourceAsStream("/resources/Robots/ZoomBot.png");

        hammerBotImage = new Image(hammerBot);
        hulkX90Image = new Image(hulkX90);
        smashBotImage = new Image(smashBot);
        spinBotImage = new Image(spinBot);
        twonkyImage = new Image(twonky);
        zoomBotImage = new Image(zoomBot);
    }

    /**
     * This function gets RoboImage from robot number
     *
     * @param figureNumber the figure number of Player
     * @return the robo image of Player
     */
    protected Image getRoboImage(int figureNumber) {
        Image roboImage = null;
        switch (figureNumber) {
            case 0 -> roboImage = this.twonkyImage;
            case 1 -> roboImage = this.smashBotImage;
            case 2 -> roboImage = this.hulkX90Image;
            case 3 -> roboImage = this.zoomBotImage;
            case 4 -> roboImage = this.hammerBotImage;
            case 5 -> roboImage = this.spinBotImage;
        }
        return roboImage;
    }


}
