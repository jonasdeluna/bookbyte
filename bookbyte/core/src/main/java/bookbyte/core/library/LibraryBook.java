package bookbyte.core.library;

import bookbyte.core.ValidationUtils;
import bookbyte.core.book.Book;
import bookbyte.core.person.Person;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a library book as a many to many field between library and book.
 * 
 */
public class LibraryBook {

    private String id;
    private Book book;
    private BookStatus status = BookStatus.AVAILABLE;
    private Person borrower = null;
    private LocalDateTime dueDate = null;

    /**
     * Constructs a new LibraryBook with the specified id and book.
     *
     * @param id   the unique identifier of the library book. Must adhere to
     *             validation rules.
     * @param book the Book instance associated with this library book.
     */
    public LibraryBook(String id, Book book) {
        ValidationUtils.validateBookID(id);
        this.id = id;
        this.book = book;
    }

    /**
     * Retrieves the unique identifier of this library book.
     *
     * @return the id of the library book.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the unique identifier for this library book.
     * 
     * <p>
     * The ID must adhere to the validation rules defined in ValidationUtils.
     * </p>
     *
     * @param id the id to set for the library book.
     */
    public void setId(String id) {
        ValidationUtils.validateBookID(id);
        this.id = id;
    }

    /**
     * Retrieves the Book instance associated with this library book.
     *
     * @return the associated Book instance.
     */
    public Book getBook() {
        return this.book;
    }

    /**
     * Sets the Book instance for this library book.
     *
     * @param book the Book instance to set for the library book.
     */
    public void setBook(Book book) {
        this.book = book;
    }


    /** Borrows the book to the specified person for the specified number of days.
     *
     * @param borrower the person to borrow the book to.
     * @param days the number of days to borrow the book for.
     */
    public void borrow(Person borrower, int days) {
        this.borrower = borrower;
        this.status = BookStatus.BORROWED;
        this.dueDate = LocalDateTime.now().plusDays(days);
    }

    /**
     * Borrows the book to the specified person until the specified date.
     * @param borrower the person to borrow the book to.
     * @param localDateTime the date until which the book is borrowed.
     */
    public void borrow(Person borrower, LocalDateTime localDateTime) {
        this.borrower = borrower;
        this.status = BookStatus.BORROWED;
        this.dueDate = localDateTime;
    }

    /**
     * Sets the status of the book to available and removes the borrower and due
     * date.
     */
    public void returnBook() {
        this.borrower = null;
        this.status = BookStatus.AVAILABLE;
        this.dueDate = null;
    }

    /**
     * Retrieves the status of the book.
     * @return the status of the book.
     */
    public BookStatus getStatus() {
        return status;
    }


    /** Retrieves the due date of the book.
     * @return the due date of the book.
     */
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    /** Retrieves the borrower of the book.
     * @return the borrower of the book.
     */
    public Person getBorrower() {
        return borrower;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LibraryBook libraryBook) {
            return libraryBook.getId().equals(this.id) && libraryBook.getBook().equals(this.book);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book);
    }
}
