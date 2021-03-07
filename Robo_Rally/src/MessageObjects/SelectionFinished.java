package MessageObjects;

/**
 * class SelectionFinished
 *
 * Implements a message that's used to inform other players about a player who has filled all five registers.
 */
public class SelectionFinished implements Message {

    final String messageType = MsgNames.selectionFinished;
    MessageBody messageBody;

    /**
     * SelectionFinished()
     * @param playerID ID of the player who has filled all five registers
     */
    public SelectionFinished(int playerID) {
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
