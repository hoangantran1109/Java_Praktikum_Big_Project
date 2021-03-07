package MessageObjects;

/**
 * class Error
 *
 * Implements a message that's used to transfer any error String
 */
public class Error implements Message {
    final String messageType = MsgNames.error;
    MessageBody messageBody;

    /**
     * Error()
     * @param error any error message
     */
    public Error(String error) {
        messageBody = new MessageBody();
        messageBody.setError(error);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
