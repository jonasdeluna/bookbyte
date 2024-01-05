package bookbyte.core.storage;

import bookbyte.core.book.Book;
import bookbyte.core.book.BookCatalog;
import bookbyte.core.library.LibraryBook;
import bookbyte.core.person.Person;
import bookbyte.core.person.PersonCatalog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class FileSaverTest {

    @Test
    public void testBooksSave() {
        File file = new File("books.json");

        Book book = new Book("The Divine Comedy", "9780393044720", "Dante Alighieri");
        Collection<Book> books = Collections.singleton(book);

        FileModelAccess<Book> fileSaver = new FileModelAccess<>(new BookSerializer(), file, Book.class);
        fileSaver.save(books);

        FileModelAccess<Book> fileAccessModel = new FileModelAccess<>(new BookSerializer(), file, Book.class);
        Collection<Book> readBooks = fileAccessModel.read();

        Assertions.assertEquals(1, readBooks.size(), "There should be 1 book in the collection");

        Book firstBook = readBooks.iterator().next();
        Assertions.assertEquals(book, firstBook, "The book should be \"The Divine Comedy\" by Dante Alighieri");
    }

    @Test
    public void testLibraryBooksSave() {
        File file = new File("library.json");

        BookCatalog bookCatalog = new BookCatalog(file);

        Book book = new Book("The Divine Comedy", "9780393044720", "Dante Alighieri");
        bookCatalog.addBook(book);

        LibraryBook libraryBook = new LibraryBook("1234567890111111", book);
        Collection<LibraryBook> libraryBooks = Collections.singleton(libraryBook);

        LibraryBookSerializer serializer = new LibraryBookSerializer(bookCatalog, new PersonCatalog());
        FileModelAccess<LibraryBook> fileSaver = new FileModelAccess<>(serializer, file, LibraryBook.class);
        fileSaver.save(libraryBooks);

        LibraryBookSerializer deserializer = new LibraryBookSerializer(bookCatalog, new PersonCatalog());
        FileModelAccess<LibraryBook> fileAccessModel = new FileModelAccess<>(deserializer, file, LibraryBook.class);

        Collection<LibraryBook> readLibraryBooks = fileAccessModel.read();

        Assertions.assertEquals(1, readLibraryBooks.size(), "There should be 1 library book in the collection");

        LibraryBook firstLibraryBook = readLibraryBooks.iterator().next();
        Assertions.assertEquals(libraryBook, firstLibraryBook,
                "The library book should be \"The Divine Comedy\" by Dante Alighieri");
    }

    @Test
    public void testPersonsSave() {
        File file = new File("persons.json");

        UUID id = UUID.randomUUID();
        Person person = new Person(id, "John", "john@doe.com");
        Collection<Person> persons = Collections.singleton(person);

        FileModelAccess<Person> fileSaver = new FileModelAccess<>(new PersonSerializer(), file, Person.class);
        fileSaver.save(persons);

        FileModelAccess<Person> fileAccessModel = new FileModelAccess<>(new PersonSerializer(), file, Person.class);
        Collection<Person> readBooks = fileAccessModel.read();

        Assertions.assertEquals(1, readBooks.size(), "There should be 1 person in the collection");

        Person firstPerson = readBooks.iterator().next();
        Assertions.assertEquals(person, firstPerson, "The person should be John Doe");
    }
}
