package MessageObjects;

/**
 * class SelectCard
 *
 * Implements a message that's used to send one
 * selected card to the server during programming phase.
 */
public class SelectCard implements Message {
    final String messageType = MsgNames.selectCard;
    MessageBody messageBody;

    /**
     * SelectCard()
     * @param card the selected card
     * @param register the register of the selected card
     */
    public SelectCard(String card, int register) {
        messageBody = new MessageBody();
        messageBody.setCard(card);
        messageBody.setRegister(register);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
