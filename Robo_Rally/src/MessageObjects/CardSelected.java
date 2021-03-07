package MessageObjects;

/**
 * class CardSelected
 *
 * Implements a message that's used to inform all clients about a assigned register of one certain player
 */
public class CardSelected implements Message {
    final String messageType = MsgNames.cardSelected;
    MessageBody messageBody;

    /**
     * CardSelected()
     * @param playerID the ID of the player whose register is assigned
     * @param register the corresponding register number (1-5)
     */
    public CardSelected(int playerID, int register) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setRegister(register);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
