package MessageObjects;

/**
 * class ConnectionUpdate
 *
 * Implements a message that's used to inform all clients about lost/regained connection of a certain player
 */
public class ConnectionUpdate implements Message {
    final String messageType = MsgNames.connectionUpdate;
    MessageBody messageBody;

    /**
     * ConnectionUpdate()
     * @param playerID ID of the affected player
     * @param connected true, if connected, false otherwise
     * @param action specifies a certain action:
     *               Remove (removes player from game), AIControl (AI takes over),
     *               Ignore (client is ignored), Reconnect (client has reestablished a connection)
     */
    public ConnectionUpdate(int playerID, boolean connected, String action) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setConnected(connected);
        messageBody.setAction(action);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
