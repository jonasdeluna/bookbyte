package bookbyte.core.book;

import bookbyte.core.storage.*;

import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a catalog for storing books.
 * Each book in the catalog is identified by its ISBN-13.
 */
public class BookCatalog {

    private final ModelAccess<Book> modelAccess;
    private final Map<String, Book> catalog = new HashMap<>();

    /**
     * Initializes an empty book catalog.
     * The catalog is saved and loaded from the endpoint provided.
     *
     * @param writeUri The endpoint to save the catalog to.
     * @param readUri  The endpoint to load the catalog from.
     */
    public BookCatalog(URI writeUri, URI readUri) {
        this.modelAccess = new RemoteModelAccess<>(writeUri, readUri, new BookSerializer(), Book.class);
    }

    /**
     * Initializes an empty book catalog.
     * The catalog is saved and loaded from the file provided.
     *
     * @param file The file to save and load the catalog from.
     */
    public BookCatalog(File file) {
        this.modelAccess = new FileModelAccess<>(new BookSerializer(), file, Book.class);
    }

    /**
     * Initializes an empty book catalog.
     * The catalog is saved and loaded from the memory only.
     */
    public BookCatalog() {
        this.modelAccess = new DirectModelAccess<>();
    }

    /**
     * Retrieves a book from the catalog using its ISBN-13.
     *
     * @param isbn13 The ISBN-13 of the book to retrieve.
     * @return The book with the provided ISBN-13, or null if the book is not found
     * in the catalog.
     */
    public Book getBookByIsbn13(String isbn13) {
        return this.catalog.get(isbn13);
    }

    /**
     * Retrieves all books from the catalog with the specified title.
     *
     * @param title The title of the books to retrieve.
     * @return A collection of books with the specified title.
     */
    public Collection<Book> getBookByTitle(String title) {
        return new ArrayList<>(this.catalog.values().stream()
                .filter(book -> book.getTitle().equals(title))
                .collect(Collectors.toList()));
    }

    /**
     * Adds a book to the catalog.
     *
     * @param book The book to add to the catalog.
     * @return True if the book was added successfully, false if the book already
     * exists in the catalog.
     */
    public Boolean addBook(Book book) {
        if (this.catalog.containsKey(book.getISBN13()))
            return false;
        this.catalog.put(book.getISBN13(), book);
        return true;
    }

    /**
     * Removes a book from the catalog.
     *
     * @param book The book to be removed from the catalog.
     * @return True if the book was removed successfully, false if the book does not
     * exist in the catalog.
     */
    public boolean removeBook(Book book) {
        if (this.catalog.containsKey(book.getISBN13())) {
            this.catalog.remove(book.getISBN13());
            return true;
        }
        return false;
    }

    /**
     * Check if a book exists.
     *
     * @param book The book to be checked.
     * @return True if the book was exists, false if the book does not
     * exist in the catalog.
     */
    public Boolean hasBook(Book book) {
        return this.catalog.containsKey(book.getISBN13());
    }

    /**
     * Check if a book exists.
     *
     * @param isbn13 The book isbn13 to be checked.
     * @return True if the book was exists, false if the book does not
     * exist in the catalog.
     */
    public Boolean hasBook(String isbn13) {
        return this.catalog.containsKey(isbn13);
    }

    /**
     * Returns the current catalog.
     *
     * @return the current book catalog.
     */
    public List<Book> getCatalog() {
        return new ArrayList<>(this.catalog.values());
    }

    /**
     * Loads books from a file into this catalog.
     */
    public void load() {
        Collection<Book> books = this.modelAccess.read();
        books.forEach(this::addBook);
    }

    /**
     * Saves the current catalog to a file.
     */
    public void save() {
        this.modelAccess.save(this.catalog.values());
    }

    /**
     * Clears the catalog.
     */
    public void clear() {
        this.catalog.clear();
    }
}
