package MessageObjects;

/**
 * class Energy
 *
 * Implements a message that's used to transmit a number of energy cubes that one player receives
 */
public class Energy implements Message {
    final String messageType = MsgNames.energy;
    MessageBody messageBody;

    /**
     * Energy()
     * @param playerID the player who gets new energy tokens
     * @param count number of energy cubes received
     */
    public Energy(int playerID, int count) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setCount(count);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
