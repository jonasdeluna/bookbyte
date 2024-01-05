package bookbyte.bookbinder;

import bookbyte.core.person.Person;
import bookbyte.core.storage.PersonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping(value = {"/person", "/person/"})
public class PersonController {
    private final Gson gson;

    public PersonController() {

        PersonSerializer personSerializer = new PersonSerializer();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Person.class, personSerializer)
                .create();
    }

    @PostMapping
    public ResponseEntity<String> createPerson(@RequestBody String personJson) {
        try {
            Person person = gson.fromJson(personJson, Person.class);

            if (BookBinderApplication.getPersonCatalog().getPersonByUUID(person.uuid()) != null) {
                return new ResponseEntity<>("The UUID already exists! Use a PUT request instead!", HttpStatus.CONFLICT);
            }

            BookBinderApplication.getPersonCatalog().addPerson(person);
            BookBinderApplication.getPersonCatalog().save();

        } catch (Exception e) {
            return new ResponseEntity<>("There was an error with the request!" + e, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Person added successfully!", HttpStatus.CREATED);
    }

    @PostMapping(value = {"/overwrite", "/overwrite/"})
    public ResponseEntity<String> overwritePersons(@RequestBody String personJson) {
        try {
            JsonArray jsonArray = JsonParser.parseString(personJson).getAsJsonArray();

            BookBinderApplication.getPersonCatalog().clear();
            for (int i = 0; i < jsonArray.size(); i++) {
                Person person = gson.fromJson(jsonArray.get(i), Person.class);
                BookBinderApplication.getPersonCatalog().addPerson(person);
            }

            BookBinderApplication.getPersonCatalog().save();

        } catch (Exception e) {
            return new ResponseEntity<>("There was an error with the request!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Book created successfully!", HttpStatus.CREATED);
    }

    @PatchMapping(value = {"/{uuid}", "/{uuid}/"})
    public ResponseEntity<String> updatePerson(@PathVariable UUID uuid, @RequestBody String personJson) {
        try {
            Person updatePerson = gson.fromJson(personJson, Person.class);
            Person currentPerson = BookBinderApplication.getPersonCatalog().getPersonByUUID(uuid);

            if (currentPerson == null) {
                return new ResponseEntity<>("The UUID does not exist!", HttpStatus.NOT_FOUND);
            }
            if (updatePerson == null) {
                return new ResponseEntity<>("There was an error with the request!", HttpStatus.BAD_REQUEST);
            }
            BookBinderApplication.getPersonCatalog().removePerson(uuid);
            BookBinderApplication.getPersonCatalog().addPerson(updatePerson);
            BookBinderApplication.getPersonCatalog().save();

        } catch (Exception e) {
            return new ResponseEntity<>("There was an error with the request!" + e, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Person updated successfully!", HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<String> updatePerson(@RequestBody String personJson) {
        try {
            Person updatePerson = gson.fromJson(personJson, Person.class);

            if (BookBinderApplication.getPersonCatalog().getPersonByUUID(updatePerson.uuid()) != null) {
                BookBinderApplication.getPersonCatalog().removePerson(updatePerson.uuid());
            }

            BookBinderApplication.getPersonCatalog().addPerson(updatePerson);
            BookBinderApplication.getPersonCatalog().save();

        } catch (Exception e) {
            return new ResponseEntity<>("There was an error with the request!" + e, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Person updated successfully!", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Collection<Person>> getAllPerson() {
        Collection<Person> allPersons = BookBinderApplication.getPersonCatalog().getPeople();
        if (allPersons.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(allPersons, HttpStatus.OK);
        }
    }

    @GetMapping(value = {"/{uuid}", "/{uuid}/"})
    public ResponseEntity<Person> getPersonByUUID(@PathVariable UUID uuid) {
        Person person = BookBinderApplication.getPersonCatalog().getPersonByUUID(uuid);
        if (person == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(person);
        }
    }

    @DeleteMapping(value = {"/{uuid}", "/{uuid}/"})
    public ResponseEntity<String> deletePerson(@PathVariable UUID uuid) {
        try {
            Person personToRemove = BookBinderApplication.getPersonCatalog().getPersonByUUID(uuid);
            if (personToRemove == null) {
                return new ResponseEntity<>("Person not found.", HttpStatus.NOT_FOUND);
            } else {
                BookBinderApplication.getPersonCatalog().removePerson(personToRemove);
                BookBinderApplication.getPersonCatalog().save();
                return new ResponseEntity<>("Person deleted successfully.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("There was an error processing your request: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
