package MessageObjects;

/**
 * class ShuffleCoding
 *
 * Implements a message that's used to inform clients that
 * the programmingDiscardPile of a player is going to be shuffled server-side.
 */
public class ShuffleCoding implements Message {
    final String messageType = MsgNames.shuffleCoding;
    MessageBody messageBody;

    /**
     * ShuffleCoding()
     * @param playerID ID of the player whose discard pile will be shuffled
     */
    public ShuffleCoding(int playerID) {
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
