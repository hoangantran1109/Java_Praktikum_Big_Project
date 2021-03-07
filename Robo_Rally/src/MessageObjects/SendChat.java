package MessageObjects;

/**
 * class SendChat
 *
 * Implements a chat message that should be sent from client to server.
 */
public class SendChat implements Message {

    final String messageType = MsgNames.sendChat;
    MessageBody messageBody;

    /**
     * SendChat()
     * @param message the chat message
     * @param to ID of the message receiver; choose to = -1 in case of a public message
     */
    public SendChat(String message, int to) {
        messageBody = new MessageBody();
        messageBody.setMessage(message);
        messageBody.setTo(to);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
