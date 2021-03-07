package MessageObjects;

/**
 * class PlayerTurning
 *
 * Implements a message that's used to inform clients about a turning robot
 */
public class PlayerTurning implements Message {

    final String messageType = MsgNames.playerTurning;
    MessageBody messageBody;

    /**
     * PlayerTurning()
     * @param playerID ID of the turning player
     * @param direction rotating direction:
     *                  clockwise, counterClockwise
     */
    public PlayerTurning(int playerID, String direction) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setDirection(direction);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
