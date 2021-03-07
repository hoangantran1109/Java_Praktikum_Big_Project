package MessageObjects;

/**
 * class StartingPointTaken
 *
 * Implements a message that should be sent to
 * all clients after a starting point has been taken.
 */
public class StartingPointTaken implements Message {
    final String messageType = MsgNames.startingPointTaken;
    MessageBody messageBody;

    /**
     * StartingPointTaken()
     * @param playerID ID of the player who has taken a starting point
     * @param position position of the starting point that has been already taken
     */
    public StartingPointTaken(int playerID, int position) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setPosition(position);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
