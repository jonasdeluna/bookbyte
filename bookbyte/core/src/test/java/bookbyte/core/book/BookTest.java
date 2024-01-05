package bookbyte.core.book;

import bookbyte.core.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book("The Divine Comedy", "9780393044720", "Michelangelo");
    }

    @Test
    public void testConstructor() {
        assertEquals("The Divine Comedy", book.getTitle());
        assertEquals("9780393044720", book.getISBN13());

    }

    @Test
    public void testConstructor_blankTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Book("", "9781234567890", "Michelangelo"));
    }

    @Test
    public void testConstructor_blankISBN13() {
        assertThrows(IllegalArgumentException.class, () -> new Book("Sample Title", "", "Michelangelo"));
    }

    @Test
    public void testGetTitle() {
        assertEquals("The Divine Comedy", book.getTitle());
    }

    @Test
    public void testGetIsbn() {
        assertEquals("9780393044720", book.getISBN13());

    }

    @Test
    public void testSetTitle() {
        book.setTitle("New Title");
        assertEquals("New Title", book.getTitle());

    }

    @Test
    public void testSetIsbn() {
        book.setISBN13("9780987654321");
        assertEquals("9780987654321", book.getISBN13());
    }

    @Test
    public void testSetISBN13_blankInput() {
        Book book = new Book("Sample Title", "9781234567890", "Michelangelo");

        assertThrows(IllegalArgumentException.class, () -> book.setISBN13(""));
    }

    @Test
    public void testSetTitle_blankInput() {
        Book book = new Book("Sample Title", "9781234567890", "Michelangelo");

        assertThrows(IllegalArgumentException.class, () -> book.setTitle(""));
    }

    @Test
    public void testValidISBN13() {
        assertDoesNotThrow(() -> ValidationUtils.validateISBN13("9781234567890"));
    }

    @Test
    public void testInvalidISBN13_Length() {
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.validateISBN13("97812345678"));
    }

    @Test
    public void testInvalidISBN13_Characters() {
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.validateISBN13("97812345678a0"));
    }

    @Test
    public void testInvalidISBN13_Empty() {
        assertThrows(IllegalArgumentException.class, () -> ValidationUtils.validateISBN13(""));
    }

    @Test
    public void testGetAuthor() {
        assertEquals("Michelangelo", book.getAuthor());
    }

    @Test
    public void testSetAuthor() {
        String newAuthor = "Dante Alighieri";
        book.setAuthor(newAuthor);
        assertEquals(newAuthor, book.getAuthor());
    }

    @Test
    public void testSetAuthor_blankInput() {
        assertThrows(IllegalArgumentException.class, () -> book.setAuthor(""), "Author name cannot be blank!");
    }

    @Test
    public void testConstructorWithCatalog() {
        BookCatalog catalog = new BookCatalog();
        String title = "Title1";
        String isbn13 = "9783161484100";
        String author = "Michelangelo";

        Book book = new Book(title, isbn13, author, catalog);

        assertEquals(title, book.getTitle());
        assertEquals(isbn13, book.getISBN13());

        assertEquals(book, catalog.getBookByIsbn13(isbn13));
    }

    @Test
    public void testConstructorWithBlankTitleOrIsbn13() {
        BookCatalog catalog = new BookCatalog();

        assertThrows(IllegalArgumentException.class, () -> new Book("", "9783161484100", "Michelangelo", catalog),
                "Title and ISBN13 cannot be blank!");
        assertThrows(IllegalArgumentException.class, () -> new Book("Title1", "", "Michelangelo", catalog),
                "Title and ISBN13 cannot be blank!");
        assertThrows(IllegalArgumentException.class, () -> new Book("", "", "Michelangelo", catalog),
                "Title and ISBN13 cannot be blank!");
    }

}
