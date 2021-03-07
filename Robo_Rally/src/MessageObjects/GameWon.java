package MessageObjects;

/**
 * class GameWon
 *
 * Implements a message that's used to inform all clients about the winner
 */
public class GameWon implements Message {
    final String messageType = MsgNames.gameWon;
    MessageBody messageBody;

    /**
     * GameWon()
     * @param playerID winner's playerID
     */
    public GameWon(int playerID) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
