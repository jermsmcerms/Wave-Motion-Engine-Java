package utils;

import com.google.gson.*;
import wavemotion.components.Component;
import wavemotion.components.Transform;
import wavemotion.entities.GameObject;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray jsonArray = jsonObject.getAsJsonArray("componentsList");
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = context.deserialize(jsonObject.get("zIndex"), int.class);

        GameObject gameObject = new GameObject(name, transform, zIndex);
        for(JsonElement item : jsonArray) {
            Component c = context.deserialize(item, Component.class);
            gameObject.addComponent(c);
        }
        return gameObject;
    }
}
