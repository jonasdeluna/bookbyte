package bookbyte.core.storage;

import bookbyte.core.book.Book;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Class used to serialize a Book object to JSON.
 */
public class BookSerializer implements Serializer<Book> {

    /**
     * Serializes a Book object to JSON.
     */
    @Override
    public JsonElement serialize(Book book, Type type, JsonSerializationContext jsonSerializationContext) {
        String title = book.getTitle();
        String isbn13 = book.getISBN13();
        String author = book.getAuthor();

        String json = "{\"title\":\"" + title + "\",\"isbn13\":\"" + isbn13 + "\",\"author\":\"" + author + "\" }";

        return new JsonParser().parse(json).getAsJsonObject();
    }

    /**
     * Deserializes a Book object from JSON.
     */
    @Override
    public Book deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        String title = jsonElement.getAsJsonObject().get("title").getAsString();
        String isbn13 = jsonElement.getAsJsonObject().get("isbn13").getAsString();
        String author = jsonElement.getAsJsonObject().get("author").getAsString();
        return new Book(title, isbn13, author);
    }
}
