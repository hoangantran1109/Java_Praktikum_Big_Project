package MessageObjects;

/**
 * class Reboot
 *
 * Implements a message that's used to inform all clients about a rebooted player
 */
public class Reboot implements Message {

    final String messageType = MsgNames.reboot;
    MessageBody messageBody;

    /**
     * Reboot()
     * @param playerID ID of the rebooted player
     */
    public Reboot(int playerID) {
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
