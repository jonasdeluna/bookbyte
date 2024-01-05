package bookbyte.core.storage;

import bookbyte.core.book.Book;
import bookbyte.core.book.BookCatalog;
import bookbyte.core.library.LibraryBook;
import bookbyte.core.person.PersonCatalog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LibraryBookSerializerTest {

    @Test
    public void testSerialize() {
        BookCatalog bookCatalog = new BookCatalog();
        Book book = new Book("The Divine Comedy", "9780393044720", "Dante Alighieri", bookCatalog);
        LibraryBook libraryBook = new LibraryBook("1234567890111111", book);
        GsonBuilder gsonBuilder = new GsonBuilder();

        LibraryBookSerializer libraryBookSerializer = new LibraryBookSerializer(bookCatalog, new PersonCatalog());

        gsonBuilder.registerTypeAdapter(LibraryBook.class, libraryBookSerializer);

        Gson gson = gsonBuilder.create();

        String jsonElement = gson.toJson(libraryBook);

        assert jsonElement != null;

        String expected = "{\"id\":\"1234567890111111\",\"isbn13\":\"9780393044720\",\"borrower\":\"null\",\"dueDate\":0}";
        Assertions.assertEquals(expected, jsonElement,
                "JSON string should be \"{\"id\":\"1234567890111111\",\"isbn13\":\"9780393044720\",\"borrower\":\"null\",\"dueDate\":0}\"");
    }

    @Test
    public void testDeserialize() {
        BookCatalog bookCatalog = new BookCatalog();
        String author = "Dante Alighieri";
        bookCatalog.addBook(new Book("The Divine Comedy", "9780393044720", author, bookCatalog));

        LibraryBookSerializer libraryBookDeserializer = new LibraryBookSerializer(bookCatalog, new PersonCatalog());

        String json = "{\"id\":\"1234567890111111\",\"isbn13\":\"9780393044720\",\"borrower\":\"null\",\"dueDate\":0}";

        JsonElement jsonElement = JsonParser.parseString(json);

        LibraryBook libraryBook = libraryBookDeserializer.deserialize(jsonElement, LibraryBook.class, null);

        assert libraryBook != null;
        Assertions.assertEquals("1234567890111111", libraryBook.getId(),
                "LibraryBook id should be \"1234567890111111\"");
        Assertions.assertEquals("The Divine Comedy", libraryBook.getBook().getTitle(),
                "Book title should be \"The Divine Comedy\"");
        Assertions.assertEquals("9780393044720", libraryBook.getBook().getISBN13(),
                "Book ISBN13 should be \"9780393044720\"");
        Assertions.assertEquals(author, libraryBook.getBook().getAuthor(), "Book author should be \"Dante Alighieri\"");
    }
}
