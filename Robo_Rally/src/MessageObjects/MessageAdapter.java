package MessageObjects;

import com.google.gson.*;
import java.lang.reflect.Type;

/**
 * @author Julian Wiemer
 * Class MessageAdapter
 *
 * Custom solution for deserializing JSON Strings of Message-implementing classes properly
 *
 * Usage:
 * gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
 */
public class MessageAdapter implements JsonDeserializer<Message> {
    @Override
    public Message deserialize(JsonElement json, Type t, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("messageType").getAsString();
        try {
            return context.deserialize(jsonObject, Class.forName("MessageObjects." + type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Element type '" + type + "' not found: ", e);
        }
    }
}
