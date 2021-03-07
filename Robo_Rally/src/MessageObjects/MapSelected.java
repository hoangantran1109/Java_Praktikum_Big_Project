package MessageObjects;

/**
 * class MapSelected
 *
 * Implements a message that's used to send the selected map from client to server
 */
public class MapSelected implements Message {
    final String messageType = MsgNames.mapSelected;
    MessageBody messageBody;

    /**
     * MapSelected()
     * @param selectedMap the selected map
     */
    public MapSelected(String[] selectedMap) {
        messageBody = new MessageBody();
        messageBody.setSelectedMap(selectedMap);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
