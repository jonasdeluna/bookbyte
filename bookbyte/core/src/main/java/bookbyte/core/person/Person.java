package bookbyte.core.person;

import java.util.UUID;

/**
 * Class used to represent a Person.
 */
public record Person(UUID uuid, String name, String email) {

    public Person {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }
}
