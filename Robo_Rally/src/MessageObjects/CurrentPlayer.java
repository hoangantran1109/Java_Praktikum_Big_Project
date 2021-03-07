package MessageObjects;

/**
 * class CurrentPlayer
 *
 * Implements message that's used to send the ID of the current player to all clients
 */
public class CurrentPlayer implements Message {
    final String messageType = MsgNames.currentPlayer;
    MessageBody messageBody;

    /**
     * CurrentPlayer()
     * @param playerID ID of the current player
     */
    public CurrentPlayer(int playerID) {
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
