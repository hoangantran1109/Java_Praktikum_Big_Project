package MessageObjects;

import java.util.List;

/**
 * class CurrentCards
 * Implements a message that's used to inform all clients about the active cards of the current register
 */
public class CurrentCards implements Message {
    final String messageType = MsgNames.currentCards;
    MessageBody messageBody;

    /**
     * CurrentCards()
     * @param activeCards list of currently active cards (also see "ActiveCard")
     */
    public CurrentCards(List<ActiveCard> activeCards ) {
        messageBody = new MessageBody();
        messageBody.setActiveCards(activeCards);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }

}
