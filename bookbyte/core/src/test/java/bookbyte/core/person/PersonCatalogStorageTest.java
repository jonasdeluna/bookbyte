package bookbyte.core.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonCatalogStorageTest {

    private TestablePersonCatalog catalog;
    private Collection<Person> simulatedDataStore;

    @BeforeEach
    public void setUp() {
        simulatedDataStore = new ArrayList<>();
        catalog = new TestablePersonCatalog();
    }

    private class TestablePersonCatalog extends PersonCatalog {
        @Override
        public void load() {
            simulatedDataStore.forEach(this::addPerson);
        }

        @Override
        public void save() {
            simulatedDataStore = new ArrayList<>(getPeople());
        }
    }

    @Test
    public void testSave() {
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "johndoe@example.com");
        catalog.addPerson(person);

        catalog.save();
        assertTrue(simulatedDataStore.contains(person));
    }

    @Test
    public void testLoad() {
        UUID uuid = UUID.randomUUID();
        Person person = new Person(uuid, "John Doe", "johndoe@example.com");
        simulatedDataStore.add(person);

        catalog.load();
        Collection<Person> people = catalog.getPeople();
        assertTrue(people.contains(person));
    }

}
