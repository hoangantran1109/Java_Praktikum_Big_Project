package MessageObjects;

/**
 * class CardsYouGotNow
 *
 * Implements message that's used to send randomly server-selected cards to one client, if that client
 * failed to choose all cards himself within time
 */
public class CardsYouGotNow implements Message {
    final String messageType = MsgNames.cardsYouGotNow;
    MessageBody messageBody;

    /**
     * CardsYouGotNow()
     * @param cards randomly server-selected programming/damage cards
     */
    public CardsYouGotNow(String[] cards) {
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
