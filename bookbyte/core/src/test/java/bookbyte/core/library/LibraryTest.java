package bookbyte.core.library;

import bookbyte.core.book.Book;
import bookbyte.core.book.BookCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    private Library library;
    private LibraryBook libraryBook1;
    private LibraryBook libraryBook2;

    @BeforeEach
    public void setUpBeforeAll() {
        library = new Library();

        BookCatalog catalog = new BookCatalog();
        Book book1 = new Book("Harry Potter and the Sorcerer's Stone", "9781338878929", "J.K. Rowling", catalog);
        Book book2 = new Book("Animal Farm", "9788129116123", "George Orwell", catalog);
        libraryBook1 = new LibraryBook("1234567890123456", book1);
        libraryBook2 = new LibraryBook("6543210987654321", book2);
    }

    @Test
    public void testAddBook() {
        // Assuming no exception is good, or you can check the size of library books.
        assertThrows(IllegalArgumentException.class, () -> library.getBook(libraryBook1.getId()));
        library.addBook(libraryBook1);
        assertEquals(libraryBook1, library.getBook(libraryBook1.getId()));
    }

    @Test
    public void testAddNullBook() {
        assertThrows(IllegalArgumentException.class, () -> library.addBook(null));
    }

    @Test
    public void testAddDuplicateBook() {
        library.addBook(libraryBook1);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(libraryBook1));
    }

    @Test
    public void testRemoveBook() {
        library.addBook(libraryBook1);
        library.removeBook(libraryBook1);
        assertThrows(IllegalArgumentException.class, () -> library.getBook(libraryBook1.getId()));
    }

    @Test
    public void testRemoveNullBook() {
        assertThrows(IllegalArgumentException.class, () -> library.removeBook(null));
    }

    @Test
    public void testRemoveNonexistentBook() {
        assertThrows(IllegalArgumentException.class, () -> library.removeBook(libraryBook1));
    }

    @Test
    public void testGetBooks() {
        assertEquals(0, library.getBooks().size());
        library.addBook(libraryBook1);
        library.addBook(libraryBook2);
        assertEquals(2, library.getBooks().size());
    }

    @Test
    public void testHasBook() {
        assertEquals(0, library.getBooks().size());
        library.addBook(libraryBook1);
        library.addBook(libraryBook2);
        assertTrue(library.hasBook(libraryBook1));
        assertTrue(library.hasBook(libraryBook1.getId()));
        library.removeBook(libraryBook1);
        assertFalse(library.hasBook(libraryBook1));
    }
}
