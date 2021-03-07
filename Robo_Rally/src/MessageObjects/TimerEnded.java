package MessageObjects;

/**
 * class TimerEnded
 *
 * Implements a message that's used to inform all clients if the timer has ended and to send
 * all playerIDs of players who didn't fill all their registers within time.
 */
public class TimerEnded implements Message {
    final String messageType = MsgNames.timerEnded;
    MessageBody messageBody;

    /**
     * TimerEnded()
     * @param playerIDs IDs of all players who didn't fill their registers within time
     */
    public TimerEnded(int[] playerIDs) {
        messageBody = new MessageBody();
        messageBody.setPlayerIDs(playerIDs);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
