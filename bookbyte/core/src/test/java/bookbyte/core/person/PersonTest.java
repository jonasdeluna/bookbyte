package bookbyte.core.person;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonTest {

    @Test
    public void testCreatePerson() {
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "john@doe.com");

        Assertions.assertNotNull(person, "Person should not be null");
        assertEquals(uuid, person.uuid(), "Person uuid should be \"" + uuid + "\"");
        assertEquals("John Doe", person.name(), "Person name should be \"John Doe\"");
        assertEquals("john@doe.com", person.email(), "Person email should be \"john@doe.com\"");
    }

    @Test
    public void testCatalogAddNull() {

        PersonCatalog catalog = new PersonCatalog();
        assertThrows(IllegalArgumentException.class, () -> {
            catalog.addPerson(null);
        });
    }

    @Test
    public void testCatalogSearch() {
        PersonCatalog catalog = new PersonCatalog();
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "john@doe.com");

        catalog.addPerson(person);

        Assertions.assertNotNull(catalog.getPersonByEmail("john@doe.com", "John Doe"), "Person should not be null");
    }

    @Test
    public void testCatalogLookup() {
        PersonCatalog catalog = new PersonCatalog();
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "john@doe.com");

        catalog.addPerson(person);

        Assertions.assertNotNull(catalog.getPersonByUUID(uuid), "Person should not be null");
    }

    @Test
    public void testCatalogRemove() {
        PersonCatalog catalog = new PersonCatalog();
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "john@doe.com");

        catalog.addPerson(person);

        Assertions.assertNotNull(catalog.getPersonByUUID(uuid), "Person should not be null");
        catalog.removePerson(uuid);
        Assertions.assertNull(catalog.getPersonByUUID(uuid), "Person should not be null");
        catalog.addPerson(person);
        Assertions.assertNotNull(catalog.getPersonByUUID(uuid), "Person should not be null");
        catalog.removePerson(person);
        Assertions.assertNull(catalog.getPersonByUUID(uuid), "Person should not be null");


    }

    @Test
    public void testPersonValidation() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Person(null, "John Doe", "john@doe.com");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Person(UUID.randomUUID(), null, "john@doe.com");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Person(UUID.randomUUID(), "John Doe", null);
        });
    }

    @Test
    public void testValidPersonCreation() {
        UUID uuid = UUID.randomUUID();
        String name = "John Doe";
        String email = "johndoe@example.com";

        Person person = new Person(uuid, name, email);

        assertEquals(uuid, person.uuid());
        assertEquals(name, person.name());
        assertEquals(email, person.email());
    }

    @Test
    public void testPersonCreationWithNullUUID() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Person(null, "John Doe", "johndoe@example.com")
        );
        assertEquals("UUID cannot be null", exception.getMessage());
    }

    @Test
    public void testPersonCreationWithBlankName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Person(UUID.randomUUID(), "", "johndoe@example.com")
        );
        assertEquals("Name cannot be null or blank", exception.getMessage());
    }

    @Test
    public void testPersonCreationWithNullName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Person(UUID.randomUUID(), null, "johndoe@example.com")
        );
        assertEquals("Name cannot be null or blank", exception.getMessage());
    }

    @Test
    public void testPersonCreationWithBlankEmail() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Person(UUID.randomUUID(), "John Doe", "")
        );
        assertEquals("Email cannot be null or blank", exception.getMessage());
    }

    @Test
    public void testPersonCreationWithNullEmail() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Person(UUID.randomUUID(), "John Doe", null)
        );
        assertEquals("Email cannot be null or blank", exception.getMessage());
    }
}
