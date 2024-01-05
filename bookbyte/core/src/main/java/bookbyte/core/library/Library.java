package bookbyte.core.library;

import bookbyte.core.book.BookCatalog;
import bookbyte.core.person.Person;
import bookbyte.core.person.PersonCatalog;
import bookbyte.core.storage.*;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The Library class represents a library that holds a collection of LibraryBooks.
 */
public class Library {

    private final ModelAccess<LibraryBook> modelAccess;
    private final Map<String, LibraryBook> libraryBooks = new HashMap<>();

    /**
     * Constructs an empty Library.
     * The library is saved and loaded from the endpoint provided.
     *
     * @param writeUri The endpoint to save the library to
     * @param readUri The endpoint to load the library from
     * @param bookCatalog The book catalog to use for loading books
     * @param personCatalog The person catalog to use for loading people
     */
    public Library(URI writeUri, URI readUri, BookCatalog bookCatalog, PersonCatalog personCatalog) {
        this.modelAccess = new RemoteModelAccess<>(writeUri, readUri, new LibraryBookSerializer(bookCatalog, personCatalog), LibraryBook.class);
    }


    /**
     * Constructs an empty Library.
     * The library is saved and loaded from the file provided.
     *
     * @param file The file to save and load the library from
     * @param bookCatalog The book catalog to use for loading books
     * @param personCatalog The person catalog to use for loading people
     */
    public Library(File file, BookCatalog bookCatalog, PersonCatalog personCatalog) {
        this.modelAccess = new FileModelAccess<>(new LibraryBookSerializer(bookCatalog, personCatalog), file, LibraryBook.class);
    }

    /**
     * Constructs an empty Library.
     * The library is saved and loaded from the memory only.
     */
    public Library() {
        this.modelAccess = new DirectModelAccess<>();
    }


    /**
     * Adds a LibraryBook to the library's collection.
     *
     * @param book the LibraryBook to be added.
     * @throws IllegalArgumentException if the provided book is null or if a book with the same ID
     *                                  is already present in the library.
     */
    public void addBook(LibraryBook book) {
        if (book == null) {
            throw new IllegalArgumentException("The book can't be null");
        }
        if (libraryBooks.containsKey(book.getId())) {
            throw new IllegalArgumentException("This book has already been added to the library");
        }

        libraryBooks.put(book.getId(), book);
    }

    /**
     * Removes a LibraryBook from the library's collection.
     *
     * @param book the LibraryBook to be removed.
     * @throws IllegalArgumentException if the provided book is null or if the book is not present
     *                                  in the library.
     */
    public void removeBook(LibraryBook book) {
        if (book == null) {
            throw new IllegalArgumentException("Can't remove a book that's null");
        }
        if (!libraryBooks.containsKey(book.getId())) {
            throw new IllegalArgumentException("The book does not exist in the library");
        }

        libraryBooks.remove(book.getId());
    }

    /**
     * Checks if a given librarybook is in the library.
     *
     * @param libraryBook the LibraryBook to be checked.
     * @return true if the book is in the library. False otherwise.
     */
    public boolean hasBook(LibraryBook libraryBook) {
        return this.libraryBooks.containsKey(libraryBook.getId());
    }

    /**
     * Checks if a given librarybook is in the library.
     *
     * @param id the id of the LibraryBook to be checked.
     * @return true if the book is in the library. False otherwise.
     */
    public boolean hasBook(String id) {
        return this.libraryBooks.containsKey(id);
    }


    /**
     * Retrieves a LibraryBook object from the library's collection based on its id.
     *
     * @param id the unique identifier of the book to retrieve.
     * @return the LibraryBook object corresponding to the given id.
     * @throws IllegalArgumentException if no book with the specified id exists in the library.
     */
    public LibraryBook getBook(String id) {
        if (!libraryBooks.containsKey(id)) {
            throw new IllegalArgumentException("The book does not exist in the library");
        }

        return libraryBooks.get(id);
    }

    /**
     * Searches for an available LibraryBook object from the library's collection based on its title.
     * @param title the title of the book to retrieve.
     * @return the LibraryBook object corresponding to the given name.
     */
    public LibraryBook getAvailableBookByTitle(String title) {
        return libraryBooks.values().stream()
                .filter(b -> b.getBook().getTitle().toLowerCase().startsWith(title.toLowerCase()) && b.getStatus() == BookStatus.AVAILABLE)
                .findFirst().orElse(null);
    }

    /**
     * Retrieves a collection of all LibraryBooks currently present in the library.
     *
     * @return a collection of LibraryBook objects in the library.
     */
    public Collection<LibraryBook> getBooks() {
        return libraryBooks.values();
    }

    /**
     * Retrieves a collection of all LibraryBooks currently available in the library.
     *
     * @return a collection of LibraryBook objects in the library.
     */
    public Collection<LibraryBook> getAvailableBooks() {
        return libraryBooks.values().stream().filter(b -> b.getStatus() == BookStatus.AVAILABLE).toList();
    }

    /**
     * Retrieves a collection of all LibraryBooks currently borrowed from the library.
     *
     * @return a collection of LibraryBook objects in the library.
     */
    public Collection<LibraryBook> getBorrowedBooks() {
        return libraryBooks.values().stream().filter(b -> b.getStatus() == BookStatus.BORROWED).toList();
    }

    /**
     * Loads books from a file into this library.
     */
    public void load() {
        for (LibraryBook book : this.modelAccess.read()) {
            addBook(book);
        }
    }

    /**
     * Saves the library to a file.
     */
    public void save() {
        this.modelAccess.save(getBooks());
    }


    /**
     * Borrows a book from the library.
     *
     * @param book   the book to borrow.
     * @param person the person borrowing the book.
     */
    public void borrowBook(LibraryBook book, Person person) {
        book.borrow(person, 14);
    }

    /**
     * Clears the library of all books.
     */
    public void clear() {
        libraryBooks.clear();
    }
}
