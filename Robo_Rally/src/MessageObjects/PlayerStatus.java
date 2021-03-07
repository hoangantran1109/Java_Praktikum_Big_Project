package MessageObjects;

/**
 * class PlayerStatus
 *
 * Implements a message that's used to inform all connected
 * clients about a player who is ready/unready to play.
 */
public class PlayerStatus implements Message {
    final String messageType = MsgNames.playerStatus;
    MessageBody messageBody;

    /**
     * PlayerStatus()
     * @param playerID ID of a ready/unready player
     * @param ready true, if player is ready to play, false otherwise
     */
    public PlayerStatus(int playerID, boolean ready) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setReady(ready);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
