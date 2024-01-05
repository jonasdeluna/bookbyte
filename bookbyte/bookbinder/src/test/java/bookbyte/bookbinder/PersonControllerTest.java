package bookbyte.bookbinder;

import bookbyte.core.person.Person;
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
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private PersonController personController = new PersonController();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
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
    public void create_person() throws Exception {
        String newPersonJson = "{\"uuid\":\"123e4567-e89b-12d3-a456-426655440000\",\"name\":\"John\",\"email\":\"john.doe@example.com\"}";

        // Perform the POST request and assert the expected response
        this.mvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPersonJson));
        //Verify that the books were added successfully
        UUID personUuid = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
        Person retrievedPerson = BookBinderApplication.getPersonCatalog().getPersonByUUID(personUuid);
        assertNotNull(retrievedPerson, "The catalog should contain the person after they're added.");
    }

    @Test
    public void create_and_delete_person() throws Exception {
        String newPersonJson = "{\"uuid\":\"113e4567-e89b-12d3-a456-426655440000\",\"name\":\"John\",\"email\":\"john.doe@example.com\"}";
        UUID personUuid = UUID.fromString("113e4567-e89b-12d3-a456-426655440000");


        // Perform the POST request and assert the expected response
        this.mvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPersonJson))
                .andExpect(status().isCreated())
                .andExpect(content().string(equalTo("Person added successfully!")));

        //Verify that the books were added successfully
        Person retrievedPerson = BookBinderApplication.getPersonCatalog().getPersonByUUID(personUuid);
        assertNotNull(retrievedPerson, "The catalog should contain the person after they're added.");

        // Perform the DELETE request and assert the expected response
        this.mvc.perform(delete(String.format("/person/%s/", personUuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPersonJson))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Person deleted successfully.")));

    }


}
