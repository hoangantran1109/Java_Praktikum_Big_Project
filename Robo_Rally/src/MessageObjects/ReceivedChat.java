package MessageObjects;

/**
 * class ReceivedChat
 *
 * Implements a chat message that should be sent from server to certain clients
 */
public class ReceivedChat implements Message {
    final String messageType = MsgNames.receivedChat;
    MessageBody messageBody;

    /**
     * ReceivedChat()
     * @param message the message to be received
     * @param from ID of the sender
     * @param isPrivate true, if it's a private message, false otherwise
     */
    public ReceivedChat(String message, int from, boolean isPrivate) {
        messageBody = new MessageBody();
        messageBody.setMessage(message);
        messageBody.setFrom(from);
        messageBody.setMessageIsPrivate(isPrivate);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
