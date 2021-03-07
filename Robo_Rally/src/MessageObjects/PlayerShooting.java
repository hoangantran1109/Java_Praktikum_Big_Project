package MessageObjects;

/**
 * class PlayerShooting
 *
 * Implements a message that's used to inform Client about currently shooting players
 */
public class PlayerShooting implements Message {
    final String messageType = MsgNames.playerShooting;
    MessageBody messageBody;

    /**
     * PlayerShooting()
     */
    public PlayerShooting() {
        messageBody = new MessageBody();
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
