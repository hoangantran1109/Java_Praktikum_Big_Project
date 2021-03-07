package MessageObjects;

/**
 * interface Message
 * @author Julian Wiemer
 * enables different implementations of messages
 */
public interface Message {
   String getMessageType();
   MessageBody getMessageBody();
}
