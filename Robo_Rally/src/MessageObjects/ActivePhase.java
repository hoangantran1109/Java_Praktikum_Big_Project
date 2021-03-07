package MessageObjects;

/**
 * class ActivePhase
 *
 * Implements a message that signals the current game phase
 */
public class ActivePhase implements Message {
    final String messageType = MsgNames.activePhase;
    MessageBody messageBody;

    /**
     * ActivePhase()
     * @param phase the current game phase:
     *              0 (starting phase), 1 (upgrade phase), 2 (programming phase), 3 (activation phase)
     */
    public ActivePhase(int phase) {
        messageBody = new MessageBody();
        messageBody.setPhase(phase);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
