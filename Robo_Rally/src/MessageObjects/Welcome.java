package MessageObjects;

/**
 * class Welcome
 *
 * Implements a message that's used to inform one client about his own playerID.
 */
public class Welcome implements Message {
    final String messageType = MsgNames.welcome;
    MessageBody messageBody;

    /**
     * Welcome()
     * @param playerID the assigned playerID
     */
    public Welcome(int playerID) {
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
