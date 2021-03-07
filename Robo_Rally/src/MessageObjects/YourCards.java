package MessageObjects;

/**
 * class YourCards
 *
 * Implements a message that's used to inform one client at the beginning of
 * every programming phase about his available hand cards.
 */
public class YourCards implements Message {
    final String messageType = MsgNames.yourCards;
    MessageBody messageBody;

    /**
     * YourCards()
     * @param cards available hand cards
     * @param cardsInPile number of cards in pile
     */
    public YourCards(String[] cards, int cardsInPile) {
        messageBody = new MessageBody();
        messageBody.setCards(cards);
        messageBody.setCardsInPile(cardsInPile);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
