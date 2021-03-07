package MessageObjects;
import GameLogic.Player;

/**
 * class PlayerAdded
 *
 * Implements a message that's used to inform all
 * clients about a new joined player
 */
public class PlayerAdded implements Message {
    final String messageType = MsgNames.playerAdded;
    MessageBody messageBody;

    /**
     * PlayerAdded()
     * @param player the new player
     */
    public PlayerAdded(Player player) {
        messageBody = new MessageBody();
        messageBody.setPlayer(player);
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
