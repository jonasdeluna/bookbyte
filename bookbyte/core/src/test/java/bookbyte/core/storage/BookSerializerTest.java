package bookbyte.core.storage;

import bookbyte.core.book.Book;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookSerializerTest {

    @Test
    public void testSerialize() {
        Book book = new Book("The Divine Comedy", "9780393044720", "Dante Alighieri");

        GsonBuilder gsonBuilder = new GsonBuilder();
        BookSerializer bookSerializer = new BookSerializer();

        gsonBuilder.registerTypeAdapter(Book.class, bookSerializer);
        Gson gson = gsonBuilder.create();

        String jsonElement = gson.toJson(book);

        Assertions.assertNotNull(jsonElement, "Serialized JSON should not be null");

        String expected = "{\"title\":\"The Divine Comedy\",\"isbn13\":\"9780393044720\",\"author\":\"Dante Alighieri\"}";

        Assertions.assertEquals(expected, jsonElement, "JSON string should match expected output");
    }

    @Test
    public void testDeserialize() {

        BookSerializer bookDeserializer = new BookSerializer();

        String json = "{\"title\":\"The Divine Comedy\",\"isbn13\":\"9780393044720\",\"author\":\"Dante Alighieri\"}";

        JsonElement jsonElement = JsonParser.parseString(json);

        Book book = bookDeserializer.deserialize(jsonElement, Book.class, null);

        Assertions.assertNotNull(book, "Deserialized book should not be null");
        Assertions.assertEquals("The Divine Comedy", book.getTitle(), "Book title should be \"The Divine Comedy\"");
        Assertions.assertEquals("9780393044720", book.getISBN13(), "Book ISBN13 should be \"9780393044720\"");
        Assertions.assertEquals("Dante Alighieri", book.getAuthor(), "Book author should be \"Dante Alighieri\"");
    }
}
