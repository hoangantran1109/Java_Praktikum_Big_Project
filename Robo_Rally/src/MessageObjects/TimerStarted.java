package MessageObjects;

/**
 * class TimerStarted
 *
 * Implements a message that's used to inform all clients that timer's running now
 */
public class TimerStarted implements Message {
    final String messageType = MsgNames.timerStarted;
    MessageBody messageBody;

    /**
     * TimerStarted()
     */
    public TimerStarted() {
        messageBody = new MessageBody();
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
