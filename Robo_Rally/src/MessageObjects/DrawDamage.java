package MessageObjects;

/**
 * class DrawDamage
 *
 * Implements a message that's used to send received damage cards to a player
 */
public class DrawDamage implements Message {
    final String messageType = MsgNames.drawDamage;
    MessageBody messageBody;

    /**
     * DrawDamage()
     * @param playerID the ID of the player to whom the cards should be sent
     * @param cards the received damage cards
     */
    public DrawDamage(int playerID, String[] cards) {
        messageBody = new MessageBody();
        messageBody.setId(playerID);
        messageBody.setCards(cards);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
