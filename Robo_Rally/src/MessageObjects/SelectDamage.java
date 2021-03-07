package MessageObjects;

/**
 * class SelectDamage
 *
 * Implements a message that's used to send selected damage cards
 * from client to server after receiving a PickDamage message.
 */
public class SelectDamage implements Message {
    final String messageType = MsgNames.selectDamage;
    MessageBody messageBody;

    /**
     * SelectDamage()
     * @param cards selected damage cards
     */
    public SelectDamage(String[] cards) {
        messageBody = new MessageBody();
        messageBody.setCards(cards);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
