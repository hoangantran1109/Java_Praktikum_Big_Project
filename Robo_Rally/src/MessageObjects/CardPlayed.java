package MessageObjects;

/**
 * class CardPlayed
 *
 * Implements a message that's used to send each card played to all clients
 */
public class CardPlayed implements Message {
    final String messageType = MsgNames.cardPlayed;
    MessageBody messageBody;

    /**
     * CardPlayed()
     * @param playerID ID of current player
     * @param card this player's currently played card
     */
    public CardPlayed(int playerID, String card) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setCard(card);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
