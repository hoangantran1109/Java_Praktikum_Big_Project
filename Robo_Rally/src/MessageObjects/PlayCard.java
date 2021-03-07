package MessageObjects;

/**
 * class PlayCard
 *
 * Implements a message that's used to transmit a (upgrade) card
 * from client to server
 */
public class PlayCard implements Message {
    final String messageType = MsgNames.playCard;
    MessageBody messageBody;

    /**
     * PlayCard()
     * @param card the card a player wants to play
     */
    public PlayCard(String card) {
        messageBody = new MessageBody();
        messageBody.setCard(card);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
