package bookbyte.core.person;

import bookbyte.core.storage.*;

import java.io.File;
import java.net.URI;
import java.util.*;

/**
 * Class used to represent a list of Person objects.
 */
public class PersonCatalog {

    private final ModelAccess<Person> modelAccess;

    private final Map<UUID, Person> people = new HashMap<>();

    /**
     * Initializes an empty Person catalog.
     * The catalog is saved and loaded from the endpoint provided.
     * @param writeUri The endpoint to save the catalog to.
     * @param readUri The endpoint to load the catalog from.
     */
    public PersonCatalog(URI writeUri, URI readUri) {
        this.modelAccess = new RemoteModelAccess<>(writeUri, readUri, new PersonSerializer(), Person.class);
    }

    /**
     * Initializes an empty Person catalog.
     * The catalog is saved and loaded from the file provided.
     * @param file The file to save and load the catalog from.
     */
    public PersonCatalog(File file) {
        this.modelAccess = new FileModelAccess<>(new PersonSerializer(), file, Person.class);
    }

    /**
     * Initializes an empty Person catalog.
     * The catalog is saved and loaded from the memory only.
     */
    public PersonCatalog() {
        this.modelAccess = new DirectModelAccess<>();
    }

    /**
     * Adds a Person to the catalog.
     *
     * @param person the Person to add
     */
    public void addPerson(Person person) {

        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }

        people.put(person.uuid(), person);
    }

    /**
     * Removes a Person to the catalog.
     *
     * @param person the Person to add
     */
    public void removePerson(Person person) {

        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }

        people.remove(person.uuid());
    }

    /**
     * Removes a Person to the catalog.
     *
     * @param uuid of the Person to add
     */
    public void removePerson(UUID uuid) {

        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }

        people.remove(uuid);
    }

    /**
     * Returns all the people in the catalog.
     *
     * @return a collection of all the people in the catalog
     */
    public Collection<Person> getPeople() {
        return new ArrayList<>(this.people.values());
    }

    /**
     * Looks up a Person by their email.
     *
     * @param email the email to look up
     * @return the Person with the provided email, or null if no such Person exists
     */
    public Person getPersonByEmail(String email, String name) {
        Person person = this.people.values().stream().filter(p -> p.email().equals(email)).findFirst().orElse(null);

        if (person == null) {
            Person newPerson = new Person(UUID.randomUUID(), name, email);
            this.addPerson(newPerson);
            return newPerson;
        }
        return person;
    }

    /**
     * Gets a person by their UUID
     *
     * @param borrower the UUID of the person
     * @return the person with the UUID
     */
    public Person getPersonByUUID(UUID borrower) {
        return this.people.get(borrower);
    }

    /**
     * Loads people from a file into the catalog.
     */
    public void load() {
        Collection<Person> books = this.modelAccess.read();
        books.forEach(this::addPerson);
    }

    /**
     * Saves the current catalog to a file.
     */
    public void save() {
        this.modelAccess.save(this.getPeople());
    }

    /**
     * Clears the catalog.
     */
    public void clear() {
        this.people.clear();
    }
}
