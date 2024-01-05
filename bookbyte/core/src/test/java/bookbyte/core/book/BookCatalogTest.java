package bookbyte.core.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookCatalogTest {

    private BookCatalog bookCatalog;
    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        bookCatalog = new BookCatalog();
        // Adding authors to book instances
        book1 = new Book("Title1", "9783161484100", "Author1", bookCatalog);
        book2 = new Book("Title2", "9783161484101", "Author2", bookCatalog);
    }

    @Test
    public void testAddBook() {
        assertTrue(new BookCatalog().addBook(book1));
        assertFalse(bookCatalog.addBook(book1));
    }

    @Test
    public void testGetBookByIsbn13() {
        BookCatalog newBookCatalog = new BookCatalog();
        assertNull(newBookCatalog.getBookByIsbn13("9783161484100"));
        newBookCatalog.addBook(book1);
        assertEquals(book1, newBookCatalog.getBookByIsbn13("9783161484100"));
    }

    @Test
    public void testGetBookByTitle() {
        BookCatalog newBookCatalog = new BookCatalog();
        Collection<Book> books = newBookCatalog.getBookByTitle("Title1");
        assertTrue(books.isEmpty());

        newBookCatalog.addBook(book1);
        books = newBookCatalog.getBookByTitle("Title1");
        assertEquals(1, books.size());
        assertTrue(books.contains(book1));
    }

    @Test
    public void testGetCatalog() {
        assertTrue(new BookCatalog().getCatalog().isEmpty());

        bookCatalog.addBook(book1);
        bookCatalog.addBook(book2);

        List<Book> catalog = bookCatalog.getCatalog();

        assertEquals(2, catalog.size());
        assertTrue(catalog.contains(book1));
        assertTrue(catalog.contains(book2));
    }

    @Test
    public void testRemoveExistingBook() {
        bookCatalog.addBook(book1);

        Boolean result = bookCatalog.removeBook(book1);

        assertTrue(result, "The book should be removed successfully");
        assertFalse(bookCatalog.getCatalog().contains(book1), "The catalog should no longer contain the book");
    }

    @Test
    public void testRemoveBookEffectOnMultipleBooks() {
        bookCatalog.addBook(book1);
        bookCatalog.addBook(book2);

        bookCatalog.removeBook(book1);

        assertTrue(bookCatalog.getCatalog().contains(book2), "The catalog should contain the remaining book");
    }

    @Test
    public void testHasBook() {
        bookCatalog.addBook(book1);
        bookCatalog.addBook(book2);
        assertTrue(bookCatalog.hasBook(book1), "The catalog should contain the remaining book");
        assertTrue(bookCatalog.hasBook(book1.getISBN13()), "The catalog should contain the remaining book");

        bookCatalog.removeBook(book1);

        assertFalse(bookCatalog.hasBook(book1), "The catalog should contain the remaining book");

    }
}
