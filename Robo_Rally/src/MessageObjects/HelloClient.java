package MessageObjects;

/**
 * class HelloClient
 *
 * Implements a message that's used to send the current protocol
 * version to the client after establishing a TCP connection
 */
public class HelloClient implements Message {
    final String messageType = MsgNames.helloClient;
    MessageBody messageBody;

    /**
     * HelloClient()
     * @param protocol the current protocol version
     */
    public HelloClient(String protocol) {
        messageBody = new MessageBody();
        messageBody.setProtocol(protocol);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
