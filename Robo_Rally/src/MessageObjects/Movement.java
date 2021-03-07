package MessageObjects;

/**
 * class Movement
 *
 * Implements a message that's used to transmit the
 * movement of a player
 */
public class Movement implements Message {
    final String messageType = MsgNames.movement;
    MessageBody messageBody;

    /**
     * Movement()
     * @param playerID ID of moving player
     * @param to this player's new position
     */
    public Movement(int playerID, int to) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setTo(to);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
