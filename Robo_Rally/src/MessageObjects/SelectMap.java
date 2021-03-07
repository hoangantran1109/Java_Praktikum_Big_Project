package MessageObjects;

/**
 * class SelectMap
 *
 * Implements a message that's used to inform the first ready player about available maps.
 */
public class SelectMap implements Message {
    final String messageType = MsgNames.selectMap;
    MessageBody messageBody;

    /**
     * SelectMap()
     * @param availableMaps all available maps
     */
    public SelectMap(String[] availableMaps) {
        messageBody = new MessageBody();
        messageBody.setAvailableMaps(availableMaps);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
