package bookbyte.core.library;

import bookbyte.core.book.Book;
import bookbyte.core.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ExtensiveLibraryTest {

    private LibraryBook libraryBook;
    private Book book;
    private Person person;

    @BeforeEach
    public void setUp() {
        book = new Book("Test Book", "1234567890123", "Author");
        libraryBook = new LibraryBook("1234567890123456", book);
        person = new Person(UUID.randomUUID(), "John Doe", "johndoe@example.com");
    }

    @Test
    public void testLibraryBookConstructor() {
        assertEquals("1234567890123456", libraryBook.getId());
        assertEquals(book, libraryBook.getBook());
        assertEquals(BookStatus.AVAILABLE, libraryBook.getStatus());
    }

    @Test
    public void testSetId() {
        String newId = "6543210987654321";
        libraryBook.setId(newId);
        assertEquals(newId, libraryBook.getId());
    }

    @Test
    public void testBorrowByDays() {
        int days = 10;
        libraryBook.borrow(person, days);
        assertEquals(BookStatus.BORROWED, libraryBook.getStatus());
        assertEquals(person, libraryBook.getBorrower());
        assertNotNull(libraryBook.getDueDate());
    }

    @Test
    public void testBorrowByDate() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(15);
        libraryBook.borrow(person, dueDate);
        assertEquals(BookStatus.BORROWED, libraryBook.getStatus());
        assertEquals(person, libraryBook.getBorrower());
        assertEquals(dueDate, libraryBook.getDueDate());
    }

    @Test
    public void testReturnBook() {
        libraryBook.borrow(person, 10);
        libraryBook.returnBook();
        assertEquals(BookStatus.AVAILABLE, libraryBook.getStatus());
        assertNull(libraryBook.getBorrower());
        assertNull(libraryBook.getDueDate());
    }

    @Test
    public void testEqualsAndHashCode() {
        LibraryBook anotherLibraryBook = new LibraryBook("1234567890123456", book);
        assertEquals(libraryBook, anotherLibraryBook);
        assertEquals(libraryBook.hashCode(), anotherLibraryBook.hashCode());
    }

}
