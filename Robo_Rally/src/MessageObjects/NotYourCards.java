package MessageObjects;

/**
 * class NotYourCards
 *
 * Implements a message that's used to inform all players about one player's
 * number of cards in hand and cards in pile
 */
public class NotYourCards implements Message {
    final String messageType = MsgNames.notYourCards;
    MessageBody messageBody;

    /**
     * NotYourCards()
     * @param playerID ID of the player
     * @param cardsInHand number of player's cards in hand
     * @param cardsInPile number of player's cards in pile
     */
    public NotYourCards(int playerID, int cardsInHand, int cardsInPile) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setCardsInHand(cardsInHand);
        messageBody.setCardsInPile(cardsInPile);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
