package MessageObjects;

import java.util.List;

/**
 * class GameStarted
 *
 * Implements a message that's used to inform all Clients
 * about a starting game and to send the chosen game board
 */
public class GameStarted implements Message {
    final String messageType = MsgNames.gameStarted;
    MessageBody messageBody;

    /**
     * GameStarted()
     * @param map the whole game board as a list of maps
     */
    public GameStarted(List<Map> map) {
        messageBody = new MessageBody();
        messageBody.setMap(map);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
