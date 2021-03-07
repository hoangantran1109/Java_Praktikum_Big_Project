package MessageObjects;

/**
 * class PlayIt
 *
 * Implements a message that's used to send a signal
 * to server that a client consents to play a card
 */
public class PlayIt implements Message {
    final String messageType = MsgNames.playIt;
    MessageBody messageBody;

    /**
     * PlayIt()
     */
    public PlayIt() {
        messageBody = new MessageBody();
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
