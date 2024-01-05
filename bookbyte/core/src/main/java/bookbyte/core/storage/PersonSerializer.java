package bookbyte.core.storage;

import bookbyte.core.person.Person;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Class used to serialize a Person object to JSON.
 */
public class PersonSerializer implements Serializer<Person> {

    @Override
    public Person deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        String id = jsonElement.getAsJsonObject().get("uuid").getAsString();
        String name = jsonElement.getAsJsonObject().get("name").getAsString();
        String email = jsonElement.getAsJsonObject().get("email").getAsString();

        return new Person(UUID.fromString(id), name, email);
    }

    @Override
    public JsonElement serialize(Person person, Type type, JsonSerializationContext jsonSerializationContext) {

        String id = person.uuid().toString();
        String name = person.name();
        String email = person.email();

        String json = "{\"uuid\":\"" + id + "\",\"name\":\"" + name + "\",\"email\":\"" + email + "\"}";
        return new JsonParser().parse(json).getAsJsonObject();
    }
}
