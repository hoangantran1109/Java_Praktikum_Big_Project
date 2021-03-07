package View;

import ClientServer.Client;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * class SelectMapController
 * This class gives the maps to choose then send it to Server
 *
 * @author Hoang An Tran, Simon Wiethase, Tobias Celik
 */
public class SelectMapController {

    public ComboBox<String> selectMap;
    public Button sendMapToServer;
    public TextArea infoArea;

    /**
     * Initialize()
     * This function gives the maps to choose
     */
    public void initialize() {
        if (!ChatGuiContext.getInstance().mapIsFix) {
            Platform.runLater(() -> {
                selectMap.setDisable(false);
                for (String mapName : ChatGuiContext.getInstance().availableMaps) {
                    selectMap.getItems().add(mapName);
                }
            });
        } else {
            Platform.runLater(() -> {
                infoArea.setText("MAP Selected:\n" + ChatGuiContext.getInstance().selectedMap +
                                 "\n\nThe Game will start when all players are ready :)");
                infoArea.setVisible(true);
                infoArea.setStyle("-fx-background-color: green");
                selectMap.setDisable(true);
                selectMap.setVisible(false);
                sendMapToServer.setDisable(true);
                sendMapToServer.setVisible(false);
            });
        }
    }

    /**
     * sendMap()
     * This function sends the selected map to the server
     */
    public void sendMap() {
        if (Client.getInstance().firstReadyPlayer.get()) {
            if (!(selectMap.getSelectionModel().getSelectedItem() == null)) {
                String mapName = selectMap.getSelectionModel().getSelectedItem();
                Context.getInstance().selectedMap = mapName;
                ChatGuiContext.getInstance().sendMapSelectedMessage(mapName);
                ((Stage) sendMapToServer.getScene().getWindow()).close();
            } else {
                Platform.runLater(() -> {
                    selectMap.setStyle("-fx-background-color: red");
                    sendMapToServer.setText("Please choose a map");
                    sendMapToServer.setStyle("-fx-background-color: red");
                });
            }
        }
    }
}