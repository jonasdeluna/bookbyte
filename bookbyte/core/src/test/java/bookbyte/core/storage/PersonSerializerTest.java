package bookbyte.core.storage;

import bookbyte.core.book.Book;
import bookbyte.core.person.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class PersonSerializerTest {

    @Test
    public void testSerialize() {
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "john@doe.com");

        GsonBuilder gsonBuilder = new GsonBuilder();
        PersonSerializer personSerializer = new PersonSerializer();

        gsonBuilder.registerTypeAdapter(Person.class, personSerializer);
        Gson gson = gsonBuilder.create();

        String jsonElement = gson.toJson(person);

        Assertions.assertNotNull(jsonElement, "Serialized JSON should not be null");

        String expected = "{\"uuid\":\"" + uuid + "\",\"name\":\"John Doe\",\"email\":\"john@doe.com\"}";

        Assertions.assertEquals(expected, jsonElement, "JSON string should match expected output");
    }

    @Test
    public void testDeserialize() {

        PersonSerializer personSerializer = new PersonSerializer();

        UUID uuid = UUID.randomUUID();
        String json = "{\"uuid\":\"" + uuid + "\",\"name\":\"John Doe\",\"email\":\"john@doe.com\"}";

        JsonElement jsonElement = JsonParser.parseString(json);

        Person person = personSerializer.deserialize(jsonElement, Book.class, null);

        Assertions.assertNotNull(person, "Deserialized person should not be null");
        Assertions.assertEquals(uuid, person.uuid(), "Person uuid should be \"" + uuid + "\"");
        Assertions.assertEquals("John Doe", person.name(), "Person name should be \"John Doe\"");
        Assertions.assertEquals("john@doe.com", person.email(), "Person email should be \"john@doe.com\"");
    }
}
