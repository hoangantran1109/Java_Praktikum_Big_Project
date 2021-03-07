package ClientServer;

import MessageObjects.Message;
import java.util.ArrayList;
import java.util.List;

/**
 * @author the whole team
 * Class ClientHandlerContext
 *
 */
public class ClientHandlerContext {

    private final static ClientHandlerContext instance = new ClientHandlerContext();
    public static ClientHandlerContext getInstance() {
        return instance;
    }
    private final List<Message> allMessages = new ArrayList<>();

    /**
     * getAllMessages()
     *
     * @return all messages
     */
    public List<Message> getAllMessages() {
        return allMessages;
    }

    /**
     * addMessage()
     *
     * @param message message to add
     */
    public void addMessage(Message message) {
        this.allMessages.add(message);
    }
}