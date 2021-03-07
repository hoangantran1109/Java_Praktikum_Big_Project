package MessageObjects;

/**
 * class HelloServer
 *
 * Implements a message that's sent from Client to Server to inform about
 * the local protocol version, group name and whether the client is an AI or not
 */
public class HelloServer implements Message {
    final String messageType = MsgNames.helloServer;
    MessageBody messageBody;

    /**
     * HelloServer()
     * @param protocol local protocol version
     * @param group group name of the client
     * @param isAI true, if client acts as an AI, false otherwise
     */
    public HelloServer(String protocol, String group, boolean isAI) {
        messageBody = new MessageBody();
        messageBody.setProtocol(protocol);
        messageBody.setGroup(group);
        messageBody.setAI(isAI);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
