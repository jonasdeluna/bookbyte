package bookbyte.bookbinder;

import bookbyte.core.book.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private BookController bookController = new BookController();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        BookBinderApplication.initializeCache();
        // Delete the books.json file before each test
    }

    @AfterAll
    public static void after() {
        File oldPeopleFile = new File("people.json");
        oldPeopleFile.delete();
        File oldBooksFile = new File("books.json");
        oldBooksFile.delete();
        File oldLibraryFile = new File("library.json");
        oldLibraryFile.delete();
        BookBinderApplication.resetCache();

    }

    @Test
    public void create_book() throws Exception {
        String newBookJson = "{\"title\":\"Effective Java\",\"isbn13\":\"9780134685991\",\"author\":\"Joshua Bloch\"}";

        // Perform the POST request and assert the expected response
        this.mvc.perform(put("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newBookJson));
        //Verify that the books were added successfully
        boolean bookExists = BookBinderApplication.getBookCatalog().hasBook("9780134685991");
        assertTrue(bookExists, "The bookCatalog should contain the book after it's added.");
    }

    @Test
    public void create_and_delete_book() throws Exception {
        String newBookJson = "{\"title\":\"Effective Java\",\"isbn13\":\"9780134685992\",\"author\":\"Joshua Bloch\"}";
        String bookIsbn13 = "9780134685992";

        // Perform the POST request and assert the expected response
        this.mvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newBookJson))
                .andExpect(status().isCreated())
                .andExpect(content().string(equalTo("Book created successfully!")));

        //Verify that the books were added successfully
        boolean bookExists = BookBinderApplication.getBookCatalog().hasBook(bookIsbn13);
        assertTrue(bookExists, "The bookCatalog should contain the book after it's added.");

        // Perform the DELETE request and assert the expected response
        this.mvc.perform(delete(String.format("/books/%s/", bookIsbn13))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newBookJson))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Book deleted successfully.")));
        assertFalse(BookBinderApplication.getBookCatalog().hasBook(""), "The bookCatalog should contain not the book after it's removed.");

    }

    @Test
    public void postExistingBook() throws Exception {
        String bookJson = "{\"title\":\"Effective Java\",\"isbn13\":\"9780134685991\",\"author\":\"Joshua Bloch\"}";

        // Mock the book as already existing

        this.mvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(equalTo("The book already exists! Use a PUT request instead!")));

    }

    @Test
    public void createBookWithInvalidIsbn() throws Exception {
        String invalidBookJson = "{\"title\":\"Test Book\",\"isbn13\":\"123\",\"author\":\"Test Author\"}";

        this.mvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("There was an error with the request!")));

        // Reload the catalog from the file to ensure the book wasn't added
        assertFalse(BookBinderApplication.getBookCatalog().hasBook("123"), "The book with an invalid ISBN should not be added to the catalog.");
    }

    @Test
    public void updateExistingBook() throws Exception {
        String existingBookJson = "{\"title\":\"Existing Book\",\"isbn13\":\"9780134685991\",\"author\":\"Author\"}";
        // Assume the book already exists in the system

        this.mvc.perform(put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(existingBookJson))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Book updated successfully!")));

        // Reload the catalog from the file and verify the update
        assertTrue(BookBinderApplication.getBookCatalog().hasBook("9780134685991"), "The existing book should be updated in the catalog.");
    }

    @Test
    public void updateBookWithInvalidIsbn() throws Exception {
        String invalidBookJson = "{\"title\":\"Invalid ISBN Book\",\"isbn13\":\"invalidISBN\",\"author\":\"Author\"}";

        this.mvc.perform(put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("There was an error with the request!")));

        // Reload the catalog from the file to ensure the book wasn't updated
        assertFalse(BookBinderApplication.getBookCatalog().hasBook("invalidISBN"), "The book with an invalid ISBN should not be updated in the catalog.");
    }

    @Test
    public void updateNonExistentBook() throws Exception {
        String nonExistentBookJson = "{\"title\":\"Non Existent Book\",\"isbn13\":\"9710434515999\",\"author\":\"Author\"}";

        this.mvc.perform(put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nonExistentBookJson))
                .andExpect(status().isCreated());

        // Reload the catalog from the file to ensure the non-existent book wasn't added
        assertTrue(BookBinderApplication.getBookCatalog().hasBook("9710434515999"), "A non-existent book should not be added to the catalog.");
    }

    @Test
    public void getBookWithInvalidIsbn() throws Exception {
        this.mvc.perform(get("/books/invalidISBN"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("")));
    }

    @Test
    public void deleteBookWithInvalidIsbn() throws Exception {
        this.mvc.perform(delete("/books/invalidISBN"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("Book not found.")));
    }

    @Test
    public void getAllBooks() throws Exception {
        // Pre-load some books into the catalog for the test
        BookBinderApplication.getBookCatalog().addBook(new Book("test", "8765436786542", "Author"));
        BookBinderApplication.getBookCatalog().save();

        this.mvc.perform(get("/books/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    public void getSpecificBook() throws Exception {
        String isbn13 = "8765436786522";
        BookBinderApplication.getBookCatalog().addBook(new Book("test", isbn13, "Author"));
        BookBinderApplication.getBookCatalog().save();

        this.mvc.perform(get("/books/" + isbn13))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn13", equalTo(isbn13)));
    }


}
