package MessageObjects;

/**
 * class PlayerValues
 *
 * Implements a message that's used to transfer basic
 * player values as name and robot from client to server
 */
public class PlayerValues implements Message {

    final String messageType = MsgNames.playerValues;
    MessageBody messageBody;

    /**
     * PlayerValues()
     * @param name player name
     * @param figure robot number:
     *               0 (TWONKEY), 1 (SQUASH BOT), 2 (HulkX90), 3 (ZOOM BOT), 4 (HAMMER BOT), 5 (SPIN BOT)
     */
    public PlayerValues(String name, int figure) {
        messageBody = new MessageBody();
        messageBody.setName(name);
        messageBody.setFigure(figure);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
