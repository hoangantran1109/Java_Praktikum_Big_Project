package MessageObjects;

/**
 * class DiscardHand
 *
 * Implements a message that's used to send the ID of a player
 * who didn't complete to fill his card registers within time
 */
public class DiscardHand implements Message {
    final String messageType = MsgNames.discardHand;
    MessageBody messageBody;

    /**
     * DiscardHand()
     * @param playerID ID of the player who didn't complete to fill his card registers within time
     */
    public DiscardHand(int playerID) {
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
