package MessageObjects;

/**
 * class SetStartingPoint
 *
 * Implements a message that should be sent from client to server to select a valid starting point.
 */
public class SetStartingPoint implements Message {
    final String messageType = MsgNames.setStartingPoint;
    MessageBody messageBody;

    /**
     * SetStartingPoint()
     * @param position the chosen starting point position
     */
    public SetStartingPoint(int position) {
        messageBody = new MessageBody();
        messageBody.setPosition(position);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
