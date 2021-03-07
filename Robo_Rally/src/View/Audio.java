package View;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


/**
 * contains initialization and functions to play music during the game
 * @author Simon Wiethase
 */
public class Audio implements Runnable
{

    private MediaPlayer mediaPlayer;
    private Boolean isMuted = true;

    /**
     * runs the game music indefinitely
     */
    public void run(){

        //from https://freemusicarchive.org/genre/Chiptune/
        //--add-modules javafx.controls,javafx.media,javafx.fxml
        //String path = "resources" + File.separator + "squareNoise.wav";
        //Media media = new Media(getClass().getResource("resources" + File.separator + "squareNoise.wav").toExternalForm());
        Media media = new Media(getClass().getResource("/resources/squareNoise.wav").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setMute(true);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.setAutoPlay(true);

        Runnable onEnd = new Runnable() {
            @Override
            public void run() {
                mediaPlayer.dispose();
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setMute(isMuted);
                mediaPlayer.setVolume(0.1);
                mediaPlayer.play();
                mediaPlayer.setOnEndOfMedia(this);
            }
        };
        mediaPlayer.setOnEndOfMedia(onEnd);
        mediaPlayer.play();
    }

    /**
     * modifies the mute attribute of the used music
     */
    public void mute(){
        if(isMuted){
            mediaPlayer.setMute(false);
            isMuted = false;
        } else {
            mediaPlayer.setMute(true);
            isMuted = true;
        }
    }
}