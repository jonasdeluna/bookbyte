package bookbyte.core.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PersonCatalogTest {

    private PersonCatalog catalog;

    @BeforeEach
    public void setUp() {
        catalog = new PersonCatalog();
    }

    @Test
    public void testAddPerson() {
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "johndoe@example.com");
        catalog.addPerson(person);

        Collection<Person> people = catalog.getPeople();
        assertTrue(people.contains(person));
    }

    @Test
    public void testRemovePersonByObject() {
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "johndoe@example.com");
        catalog.addPerson(person);
        catalog.removePerson(person);

        Collection<Person> people = catalog.getPeople();
        assertFalse(people.contains(person));
    }

    @Test
    public void testRemovePersonByUUID() {
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "johndoe@example.com");
        catalog.addPerson(person);
        catalog.removePerson(uuid);

        Collection<Person> people = catalog.getPeople();
        assertFalse(people.contains(person));
    }

    @Test
    public void testGetPersonByEmail() {
        UUID uuid = UUID.randomUUID();
        String email = "johndoe@example.com";
        Person person = new Person(uuid, "John Doe", email);
        catalog.addPerson(person);

        Person retrievedPerson = catalog.getPersonByEmail(email, "John Doe");
        assertEquals(person, retrievedPerson);
    }

    @Test
    public void testGetPersonByEmailNotPresent() {
        String email = "janedoe@example.com";
        Person retrievedPerson = catalog.getPersonByEmail(email, "Jane Doe");
        assertNotNull(retrievedPerson);
        assertEquals(email, retrievedPerson.email());
    }

    @Test
    public void testGetPersonByUUID() {
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "johndoe@example.com");
        catalog.addPerson(person);

        Person retrievedPerson = catalog.getPersonByUUID(uuid);
        assertEquals(person, retrievedPerson);
    }

    @Test
    public void testClear() {
        catalog.addPerson(new Person(UUID.randomUUID(), "John Doe", "johndoe@example.com"));
        catalog.clear();

        assertTrue(catalog.getPeople().isEmpty());
    }

    // Additional tests for exception handling
    @Test
    public void testAddNullPerson() {
        assertThrows(IllegalArgumentException.class, () -> catalog.addPerson(null));
    }

    @Test
    public void testRemoveNullPerson() {
        assertThrows(IllegalArgumentException.class, () -> catalog.removePerson((Person) null));
    }

    @Test
    public void testRemovePersonWithNullUUID() {
        assertThrows(IllegalArgumentException.class, () -> catalog.removePerson((UUID) null));
    }
}
