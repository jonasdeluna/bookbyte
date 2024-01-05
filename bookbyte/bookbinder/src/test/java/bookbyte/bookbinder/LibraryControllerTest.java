package bookbyte.bookbinder;

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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private PersonController personController = new PersonController();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Delete the books.json file before each test
        BookBinderApplication.initializeCache();

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
    public void create_library_book() throws Exception {
        String newBookJson = "{\"title\":\"Effective Java\",\"isbn13\":\"9290134685991\",\"author\":\"Joshua Bloch\"}";
        // Perform the POST request and assert the expected response
        this.mvc.perform(put("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newBookJson));
        //Verify that the books were added successfully

        String libraryJson = "{\"id\":\"1234567891234567\",\"isbn13\":\"9290134685991\"}";

        //Add the book to the library
        this.mvc.perform(put("/library/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(libraryJson))
                .andExpect(status().isCreated());
        boolean libraryBookExists = BookBinderApplication.getLibrary().hasBook("1234567891234567");
        assertTrue(libraryBookExists, "The library should contain the book after it's added.");


    }


}
