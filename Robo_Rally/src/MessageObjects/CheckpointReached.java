package MessageObjects;

/**
 * class CheckpointReached
 *
 * Implements message that's used to inform all clients about a player who has reached a certain checkpoint
 */
public class CheckpointReached implements Message {
    final String messageType = MsgNames.checkpointReached;
    MessageBody messageBody;

    /**
     * CheckpointReached()
     * @param playerID ID of the player who has reached a new checkpoint
     * @param number the number of reached checkpoints
     */
    public CheckpointReached(int playerID, int number) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setNumber(number);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
