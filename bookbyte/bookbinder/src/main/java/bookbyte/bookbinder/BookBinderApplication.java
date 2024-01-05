package bookbyte.bookbinder;

import bookbyte.core.book.BookCatalog;
import bookbyte.core.library.Library;
import bookbyte.core.person.PersonCatalog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;

/**
 * The BookBinderApplication class is the main class for the BookBinder application.
 * Contains caching for the BookCatalog, PersonCatalog, and Library.
 */
@SpringBootApplication
public class BookBinderApplication {

    private static final String BOOKS_FILE = "books.json";
    private static final String LIBRARY_FILE = "library.json";
    private static final String PEOPLE_FILE = "people.json";

    private static BookCatalog bookCatalog;
    private static PersonCatalog personCatalog;
    private static Library library;


    public static void main(String[] args) {

        initializeCache();

        SpringApplication.run(BookBinderApplication.class, args);
    }

    public static void initializeCache() {
        final File booksFile = new File(BOOKS_FILE);
        final File libraryFile = new File(LIBRARY_FILE);
        final File peopleFile = new File(PEOPLE_FILE);
        bookCatalog = new BookCatalog(booksFile);
        personCatalog = new PersonCatalog(peopleFile);
        library = new Library(libraryFile, bookCatalog, personCatalog);
        bookCatalog.load();
        personCatalog.load();
        library.load();

    }

    public static void resetCache() {
        bookCatalog.clear();
        personCatalog.clear();
        library.clear();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Server started on port 8080");


        };
    }

    /**
     * Returns the BookCatalog instance used by the application.
     *
     * @return the BookCatalog instance used by the application.
     */
    public static BookCatalog getBookCatalog() {
        return bookCatalog;
    }

    /**
     * Returns the Library instance used by the application.
     *
     * @return the Library instance used by the application.
     */
    public static Library getLibrary() {
        return library;
    }

    /**
     * Returns the PersonCatalog instance used by the application.
     *
     * @return the PersonCatalog instance used by the application.
     */
    public static PersonCatalog getPersonCatalog() {
        return personCatalog;
    }
}