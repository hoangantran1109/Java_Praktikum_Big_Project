package MessageObjects;

/**
 * class PickDamage
 *
 * Implements a message that's used to inform one client that
 * he has to pick a number of damage cards.
 * This message should only be sent if there are no spam cards left.
 */
public class PickDamage implements Message {
    final String messageType = MsgNames.pickDamage;
    MessageBody messageBody;

    /**
     * PickDamage()
     * @param count the number of damage cards to be picked by the client
     */
    public PickDamage(int count) {
        messageBody = new MessageBody();
        messageBody.setCount(count);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
