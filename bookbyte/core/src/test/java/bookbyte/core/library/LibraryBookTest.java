package bookbyte.core.library;

import bookbyte.core.book.Book;
import bookbyte.core.book.BookCatalog;
import bookbyte.core.person.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryBookTest {

    BookCatalog bookCatalog = new BookCatalog();

    @Test
    public void testConstructor_ValidParameters() {
        Book book = new Book("Sample Title", "1234567891234", "Author Name", bookCatalog);
        LibraryBook libraryBook = new LibraryBook("1234567890123456", book);

        assertEquals("1234567890123456", libraryBook.getId());
        assertEquals(book, libraryBook.getBook());
    }

    @Test
    public void testConstructor_RemoveDashes() {
        Book book = new Book("Sample Title", "123-4567-891-234", "Author Name", bookCatalog);
        LibraryBook libraryBook = new LibraryBook("1234567890123456", book);

        assertEquals("1234567890123456", libraryBook.getId());
        assertEquals(book, libraryBook.getBook());
    }

    @Test
    public void testConstructor_InvalidID() {
        Book book = new Book("Sample Title", "1234567891234", "Author Name", bookCatalog);
        assertThrows(IllegalArgumentException.class, () -> new LibraryBook("12345", book));
    }

    @Test
    public void testSetId_ValidID() {
        Book book = new Book("Sample Title", "1234567891234", "Author Name", bookCatalog);
        LibraryBook libraryBook = new LibraryBook("1234567890123456", book);

        libraryBook.setId("6543210987654321");
        assertEquals("6543210987654321", libraryBook.getId());
    }

    @Test
    public void testSetId_InvalidID() {
        Book book = new Book("Sample Title", "1234567891234", "Author Name", bookCatalog);
        LibraryBook libraryBook = new LibraryBook("1234567890123456", book);

        assertThrows(IllegalArgumentException.class, () -> libraryBook.setId("12345"));
    }

    @Test
    public void testSetBook_ValidBook() {
        Book book = new Book("Sample Title", "1234567891234", "Author Name", bookCatalog);
        LibraryBook libraryBook = new LibraryBook("1234567890123456", book);

        Book newBook = new Book("Another Title", "1234567891234", "Another Author Name", bookCatalog);
        libraryBook.setBook(newBook);
        assertEquals(newBook, libraryBook.getBook());
    }

    @Test
    public void testBorrowBook() {
        Book book = new Book("Sample Title", "1234567891234", "Author Name", bookCatalog);
        LibraryBook libraryBook = new LibraryBook("1234567890123456", book);

        Person person = new Person(UUID.randomUUID(), "John Doe", "john@doe.com");
        libraryBook.borrow(person, 15);

        assertEquals(person, libraryBook.getBorrower());
        assertEquals(LocalDateTime.now().plusDays(15).getDayOfMonth(), libraryBook.getDueDate().getDayOfMonth());
        assertEquals(BookStatus.BORROWED, libraryBook.getStatus());
    }

    @Test
    public void testReturnBook() {
        Book book = new Book("Sample Title", "1234567891234", "Author Name", bookCatalog);
        LibraryBook libraryBook = new LibraryBook("1234567890123456", book);

        Person person = new Person(UUID.randomUUID(), "John Doe", "john@doe.com");
        libraryBook.borrow(person, 15);
        libraryBook.returnBook();

        assertNull(libraryBook.getBorrower());
        assertNull(libraryBook.getDueDate());
        assertEquals(BookStatus.AVAILABLE, libraryBook.getStatus());
    }
}
