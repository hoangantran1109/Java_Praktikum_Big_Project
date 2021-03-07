package MessageObjects;

/**
 * class SetStatus
 *
 * Implements a message that's used to signal (un-)readiness to the server.
 */
public class SetStatus implements Message {

    final String messageType = MsgNames.setStatus;
    MessageBody messageBody;

    /**
     * SetStatus()
     * @param ready true, if player's ready, false otherwise
     */
    public SetStatus(boolean ready) {
        messageBody = new MessageBody();
        messageBody.setReady(ready);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
