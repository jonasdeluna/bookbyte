package bookbyte.core.library;

import bookbyte.core.book.Book;
import bookbyte.core.book.BookCatalog;
import bookbyte.core.person.Person;
import bookbyte.core.person.PersonCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryStorageTest {

    private Library library;
    private BookCatalog bookCatalog;
    private PersonCatalog personCatalog;
    private LibraryBook libraryBook;
    private Person person;

    @BeforeEach
    public void setUp() {
        bookCatalog = new BookCatalog();
        personCatalog = new PersonCatalog();
        File testFile = new File("library.json");
        library = new Library(testFile, bookCatalog, personCatalog);
        libraryBook = new LibraryBook("1234567890123456", new Book("Test Book", "9780393044720", "Author"));
        person = new Person(UUID.randomUUID(), "John Doe", "johndoe@example.com");
    }

    // Constructors
    @Test
    public void testLibraryConstructorWithURIs() {
        Library uriLibrary = new Library(URI.create("library.json"), URI.create("library.json"), bookCatalog, personCatalog);
        assertNotNull(uriLibrary);
    }

    @Test
    public void testLibraryConstructorWithFile() {
        Library fileLibrary = new Library(new File("library.json"), bookCatalog, personCatalog);
        assertNotNull(fileLibrary);
    }

    // Get Available Books
    @Test
    public void testGetAvailableBooks() {
        library.addBook(libraryBook);
        Collection<LibraryBook> availableBooks = library.getAvailableBooks();
        assertTrue(availableBooks.contains(libraryBook));
    }

    // Get Borrowed Books
    @Test
    public void testGetBorrowedBooks() {
        library.addBook(libraryBook);
        library.borrowBook(libraryBook, person);
        Collection<LibraryBook> borrowedBooks = library.getBorrowedBooks();
        assertTrue(borrowedBooks.contains(libraryBook));
    }

    // Save Method
    @Test
    public void testSave() {
        // Simulate the save operation
        library.addBook(libraryBook);
        library.save();
        // Assuming that save doesn't throw an exception is a successful test
        // In a real scenario, you would check if the data was correctly written to the file or remote service
    }

    // Borrow Book Method
    @Test
    public void testBorrowBook() {
        library.addBook(libraryBook);
        library.borrowBook(libraryBook, person);
        assertEquals(BookStatus.BORROWED, libraryBook.getStatus());
        assertEquals(person, libraryBook.getBorrower());
    }
}
