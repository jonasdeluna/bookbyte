package bookbyte.core.storage;

import bookbyte.core.book.Book;
import bookbyte.core.book.BookCatalog;
import bookbyte.core.library.LibraryBook;
import bookbyte.core.person.PersonCatalog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.Collection;

public class FileModelAccessTest {

    @Test
    public void testReadBooks() {
        // Including the author parameter in the Book object creation
        Book book = new Book("The Divine Comedy", "9780393044720", "Dante Alighieri");

        URL url = Thread.currentThread().getContextClassLoader().getResource("books.json");
        Assertions.assertNotNull(url, "URL should not be null");
        File file = new File(url.getPath());

        BookSerializer deserializer = new BookSerializer();
        FileModelAccess<Book> bookReader = new FileModelAccess<>(deserializer, file, Book.class);

        Collection<Book> books = bookReader.read();
        Assertions.assertEquals(1, books.size(), "There should be 1 book in the collection");

        Book firstBook = books.iterator().next();
        Assertions.assertEquals(book, firstBook, "The book should be \"The Divine Comedy\" by Dante Alighieri");
    }

    @Test
    public void testReadLibraryBooks() {
        // Including the author parameter in the Book object creation
        Book book = new Book("The Divine Comedy", "9780393044720", "Dante Alighieri");

        BookCatalog bookCatalog = new BookCatalog();
        bookCatalog.addBook(book);

        LibraryBook libraryBook = new LibraryBook("1234567890111111", book);

        URL url = Thread.currentThread().getContextClassLoader().getResource("library.json");
        Assertions.assertNotNull(url, "URL should not be null");
        File file = new File(url.getPath());

        LibraryBookSerializer deserializer = new LibraryBookSerializer(bookCatalog, new PersonCatalog());
        FileModelAccess<LibraryBook> libraryBookReader = new FileModelAccess<>(deserializer, file, LibraryBook.class);

        Collection<LibraryBook> libraryBooks = libraryBookReader.read();
        Assertions.assertEquals(1, libraryBooks.size(), "There should be 1 library book in the collection");

        LibraryBook firstLibraryBook = libraryBooks.iterator().next();
        Assertions.assertEquals(libraryBook, firstLibraryBook,
                "The library book should be \"The Divine Comedy\" by Dante Alighieri");
    }
}
