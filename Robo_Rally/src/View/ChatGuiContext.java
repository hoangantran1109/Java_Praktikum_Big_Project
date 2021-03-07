package View;

import ClientServer.Client;
import MessageObjects.MapSelected;
import MessageObjects.Message;
import MessageObjects.MessageAdapter;
import MessageObjects.SetStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * facilitates the exchange between Client, ChatGuiController and SelectMapController
 * @author Tobias Celik, Hoang An Tran
 */
public class ChatGuiContext {

    private static final ChatGuiContext chatGuiContext = new ChatGuiContext();
    private Gson gson;
    private Client client;
    protected List<String> availableMaps = new ArrayList<>();
    protected boolean mapIsFix = false;
    protected String selectedMap = "";
    private static final Logger logger = LogManager.getLogger(ChatGuiContext.class);

    public static ChatGuiContext getInstance() {
        return chatGuiContext;
    }

    /**
     * initializes the GSON instance to send messages with, passed the client from parameter to the class client
     * @param client used to assign a client to the ChatGuiContext
     */
    public void initialize(Client client) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
        gson = gsonBuilder.create();
        this.client = client;
    }

    /**
     * sends message of the type status to the client out stream to indicate readiness
     * @param maps the value indicating readiness that is send forth
     */
    public void aiSendsRandomMap(String[] maps) {
        int index = new Random().nextInt(maps.length);
        this.mapIsFix = true;
        this.selectedMap = maps[index];
        sendMapSelectedMessage(maps[index]);
    }

    protected void sendReadyMessage(boolean status) {
        Message setStatus = new SetStatus(status);
        String setStatusJson = gson.toJson(setStatus);
        client.out.println(setStatusJson);
        logger.info(setStatusJson);
    }

    /**
     * sends message of type map selected to the client out stream
     * @param selectedMap the name of the selected map
     */
    protected void sendMapSelectedMessage(String selectedMap) {
        this.selectedMap = selectedMap;
        Message mapSelected = new MapSelected(new String[]{selectedMap});
        String mapSelectedJson = gson.toJson(mapSelected);
        client.out.println(mapSelectedJson);
        mapIsFix = true;
        logger.info(mapSelectedJson);
    }
}
