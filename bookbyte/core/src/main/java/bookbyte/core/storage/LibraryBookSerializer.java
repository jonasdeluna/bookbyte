package bookbyte.core.storage;

import bookbyte.core.book.Book;
import bookbyte.core.book.BookCatalog;
import bookbyte.core.library.LibraryBook;
import bookbyte.core.person.Person;
import bookbyte.core.person.PersonCatalog;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
    * Class used to serialize a LibraryBook object to JSON.
 */
public class LibraryBookSerializer implements Serializer<LibraryBook> {

    private final BookCatalog bookCatalog;
    private final PersonCatalog catalog;

    /**
     * Constructor for LibraryBookSerializer.
     * The bookCatalog and personCatalog is only needed for deserialization.
     *
     * @param bookCatalog the BookCatalog instance to use for retrieving books.
     * @param personCatalog the PersonCatalog instance to use for retrieving people.
     */
    public LibraryBookSerializer(BookCatalog bookCatalog, PersonCatalog personCatalog) {
        this.bookCatalog = bookCatalog;
        this.catalog = personCatalog;
    }

    /**
        * Serializes a LibraryBook object to JSON.
     */
    @Override
    public JsonElement serialize(LibraryBook libraryBook, Type type, JsonSerializationContext jsonSerializationContext) {

        String id = libraryBook.getId();
        String isbn13 = libraryBook.getBook().getISBN13();
        String borrower = libraryBook.getBorrower() == null ? null : libraryBook.getBorrower().uuid().toString();
        long dueDate = libraryBook.getDueDate() == null ? 0 : libraryBook.getDueDate().toEpochSecond(ZoneOffset.UTC);

        String json = "{\"id\":\"" + id + "\",\"isbn13\":\"" + isbn13 + "\",\"borrower\":\"" + borrower + "\",\"dueDate\":" + dueDate + "}";

        return new JsonParser().parse(json).getAsJsonObject();
    }

    /**
     * Deserializes a LibraryBook object from JSON.
     */
    @Override
    public LibraryBook deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        String isbn13 = jsonElement.getAsJsonObject().get("isbn13").getAsString();
        String id = jsonElement.getAsJsonObject().get("id").getAsString();

        String borrower;
        if (jsonElement.getAsJsonObject().get("borrower").isJsonNull()) {
            borrower = null;
        } else {
            borrower = jsonElement.getAsJsonObject().get("borrower").getAsString();
        }
        long dueDate = jsonElement.getAsJsonObject().get("dueDate").getAsLong();

        Book book = bookCatalog.getBookByIsbn13(isbn13);
        if (book == null) {
            throw new JsonParseException("Book with ISBN13 " + isbn13 + " not found in catalog.");
        }
        LibraryBook libraryBook = new LibraryBook(id, book);
        if (borrower != null && !borrower.equals("null")) {
            Person person = catalog.getPersonByUUID(UUID.fromString(borrower));
            if (person != null) {
                libraryBook.borrow(person, LocalDateTime.ofEpochSecond(dueDate, 0, ZoneOffset.UTC));
            }
        }

        return libraryBook;
    }
}
