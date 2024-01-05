package bookbyte.core.book;

import bookbyte.core.ValidationUtils;

/**
 * Represents a book with properties like title and ISBN13.
 * 
 */

public class Book {

    private String isbn13;
    private String title;
    private String author;

    /**
     * Constructs a new Book with the specified title and ISBN13.
     *
     * @param title  the title of the book. Must not be blank.
     * @param isbn13 the 13-digit International Standard Book Number (ISBN) of the
     *               book. Must not be blank.
     * @param author of the book. Must not be blank.
     * @throws IllegalArgumentException if the provided title or ISBN13 is blank.
     */

    public Book(String title, String isbn13, String author) {
        if (title.isBlank() || isbn13.isBlank() || author.isBlank()) {
            throw new IllegalArgumentException("Title, Author, or ISBN13 cannot be blank!");
        }
        ValidationUtils.validateISBN13(isbn13);
        this.title = title;
        this.isbn13 = isbn13;
        this.author = author;
    }

    /**
     * Constructs a new Book with the specified title and ISBN13.
     *
     * @param title   the title of the book. Must not be blank.
     * @param isbn13  the 13-digit International Standard Book Number (ISBN) of the
     *                book. Must not be blank.
     * @param author  of the book. Must not be blank.
     * @param catalog the catalog to add this book to upon construction.
     * @throws IllegalArgumentException if the provided title or ISBN13 is blank.
     */

    public Book(String title, String isbn13, String author, BookCatalog catalog) {
        if (title.isBlank() || isbn13.isBlank()) {
            throw new IllegalArgumentException("Title and ISBN13 cannot be blank!");
        }
        ValidationUtils.validateISBN13(isbn13);
        this.title = title;
        this.isbn13 = isbn13;
        this.author = author;
        catalog.addBook(this);
    }

    /**
     * Retrieves the ISBN13 of this book.
     *
     * @return the 13-digit International Standard Book Number (ISBN) of the book.
     */
    public String getISBN13() {
        return this.isbn13;
    }

    /**
     * Sets the ISBN13 of this book.
     *
     * @param isbn13 the 13-digit International Standard Book Number (ISBN) to set
     *               for the book. Must not be blank.
     * @throws IllegalArgumentException if the provided isbn13 is more than 13
     *                                  characters long/contains anything but
     *                                  numerical values.
     */
    public void setISBN13(String isbn13) {
        if (isbn13.isBlank()) {
            throw new IllegalArgumentException("ISBN13 cannot be blank");
        }
        ValidationUtils.validateISBN13(isbn13);
        this.isbn13 = isbn13;
    }

    /**
     * Retrieves the title of this book.
     *
     * @return the title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this book.
     *
     * @param title the title to set for the book. Must not be blank.
     * @throws IllegalArgumentException if the provided title is blank.
     */
    public void setTitle(String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank.");
        }
        this.title = title;
    }

    /**
     * Returns the author's name.
     *
     * @return A String representing the author's name.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author's name.
     *
     * @param author A String containing the name of the author to be set.
     */
    public void setAuthor(String author) {
        if (author.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank.");
        }
        this.author = author;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Book book) {
            return this.isbn13.equals(book.getISBN13());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return isbn13.hashCode();
    }

}
