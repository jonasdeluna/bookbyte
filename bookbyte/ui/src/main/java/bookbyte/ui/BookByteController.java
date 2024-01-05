package bookbyte.ui;

import bookbyte.core.ValidationUtils;
import bookbyte.core.book.Book;
import bookbyte.core.book.BookCatalog;
import bookbyte.core.library.BookStatus;
import bookbyte.core.library.Library;
import bookbyte.core.library.LibraryBook;
import bookbyte.core.person.PersonCatalog;
import bookbyte.core.storage.RemoteModelAccess;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URI;

/**
 * The BookByteController class is the controller for the BookByte application.
 */

public class BookByteController {

    private BookCatalog bookCatalog;
    private Library library;
    private PersonCatalog personCatalog;


    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField authorField;
    @FXML
    private ListView<String> bookList;
    @FXML
    private Button addBookButton;
    @FXML
    private Button removeBookButton;
    @FXML
    private TextField emailField;
    @FXML
    private TextField personNameField;
    @FXML
    private Button borrowButton;
    @FXML
    private ListView<String> availableBooksList;
    @FXML
    private Button returnBookButton;
    @FXML
    private ListView<String> borrowedBooksList;
    @FXML
    private TextField availabilityField;
    @FXML
    private Text availabilityStatus;
    @FXML
    private Button availabilityCheckButton;

    public BookByteController() {}

    /**
     * Initializes the controller.
     */
    public void initialize() {
        addBookButton.setOnAction(this::handleAddBook);
        removeBookButton.setOnAction(this::handleRemoveBook);
        borrowButton.setOnAction(this::handleBorrowBook);
        returnBookButton.setOnAction(this::handleReturnBook);
        availabilityCheckButton.setOnAction(this::searchAvailability);

        URI uri = URI.create("http://localhost:8080/books/");
        if (RemoteModelAccess.isServerRunning(uri)) {

            URI booksWriteUri = URI.create("http://localhost:8080/books/overwrite/");
            URI libraryWriteUri = URI.create("http://localhost:8080/library/overwrite/");
            URI peopleWriteUri = URI.create("http://localhost:8080/person/overwrite/");

            URI booksReadUri = URI.create("http://localhost:8080/books/");
            URI libraryReadUri = URI.create("http://localhost:8080/library/");
            URI peopleReadUri = URI.create("http://localhost:8080/person/");

            this.bookCatalog = new BookCatalog(booksWriteUri, booksReadUri);
            this.personCatalog = new PersonCatalog(peopleWriteUri, peopleReadUri);
            this.library = new Library(libraryWriteUri, libraryReadUri, bookCatalog, personCatalog);

        } else {
            final File booksFile = new File("src/main/resources/bookbyte/ui/books.json");
            final File libraryFile = new File("src/main/resources/bookbyte/ui/library.json");
            final File peopleFile = new File("src/main/resources/bookbyte/ui/people.json");

            this.bookCatalog = new BookCatalog(booksFile);
            this.personCatalog = new PersonCatalog(peopleFile);
            this.library = new Library(libraryFile, bookCatalog, personCatalog);
        }

        personCatalog.load();
        bookCatalog.load();
        library.load();

        updateBookLists();
    }

    /**
     * Formats a book for display in the book list.
     * @param book the book to format
     * @return the formatted book
     */
    private String formatBook(LibraryBook book) {

        if (book.getStatus() == BookStatus.BORROWED && book.getBorrower() != null) {
            return String.format("(%s) %s - %s by %s (borrowed by %s, %s)", book.getId(), book.getBook().getTitle(),
                    book.getBook().getISBN13(), book.getBook().getAuthor(), book.getBorrower().name(), book.getDueDate());
        }

        return String.format("(%s) %s - %s by %s", book.getId(), book.getBook().getTitle(),
                book.getBook().getISBN13(), book.getBook().getAuthor());
    }


    /**
     * Updates the book lists.
     */
    public void updateBookLists() {
        bookList.getItems().clear();
        for (LibraryBook book : library.getBooks()) {
            ObservableList<String> items = bookList.getItems();
            items.add(formatBook(book));
        }

        availableBooksList.getItems().clear();
        for (LibraryBook book : library.getAvailableBooks()) {
            ObservableList<String> items = availableBooksList.getItems();
            items.add(formatBook(book));
        }

        borrowedBooksList.getItems().clear();
        for (LibraryBook book : library.getBorrowedBooks()) {
            ObservableList<String> items = borrowedBooksList.getItems();
            items.add(formatBook(book));
        }
    }

    /**
     * Adds a book to the library.
     * @param event the event that triggered this method
     */
    private void handleAddBook(ActionEvent event) {
        ValidationUtils.validateISBN13(isbnField.getText());
        ValidationUtils.validateBookID(idField.getText());
        if (nameField.getText().isBlank())
            throw new IllegalArgumentException("Title cannot be blank.");
        Book addedBook = new Book(nameField.getText(), isbnField.getText(), authorField.getText(), bookCatalog);
        LibraryBook libBook = new LibraryBook(idField.getText(), addedBook);
        library.addBook(libBook);

        updateBookLists();

        save();
    }

    /**
     * Removes a book from the library.
     * @param event the event that triggered this method
     */
    private void handleRemoveBook(ActionEvent event) {
        String selectedBook = bookList.getSelectionModel().getSelectedItem();
        if (selectedBook == null)
            throw new IllegalAccessError("Cannot remove an empty book");
        String id = selectedBook.substring(selectedBook.indexOf("(") + 1, selectedBook.indexOf(")"));
        library.removeBook(library.getBook(id));

        updateBookLists();

        save();
    }


    /**
     * Handles the borrow book button.
     * @param event the event that triggered this method
     */
    private void handleBorrowBook(ActionEvent event) {
        String selectedBook = availableBooksList.getSelectionModel().getSelectedItem();
        if (selectedBook == null)
            throw new IllegalAccessError("Cannot borrow an empty book");
        String id = selectedBook.substring(selectedBook.indexOf("(") + 1, selectedBook.indexOf(")"));
        String email = emailField.getText();
        ValidationUtils.validateEmail(email);
        String name = personNameField.getText();
        library.borrowBook(library.getBook(id), personCatalog.getPersonByEmail(email, name));

        updateBookLists();

        save();
    }


    /**
     * Handles the return book button.
     * @param event the event that triggered this method
     */
    private void handleReturnBook(ActionEvent event) {
        String selectedBook = borrowedBooksList.getSelectionModel().getSelectedItem();
        if (selectedBook == null)
            throw new IllegalAccessError("Cannot return an empty book");
        String id = selectedBook.substring(selectedBook.indexOf("(") + 1, selectedBook.indexOf(")"));
        library.getBook(id).returnBook();

        updateBookLists();

        save();
    }

    /**
     * Searches for the availability of a book.
     * @param event the event that triggered this method
     */
    private void searchAvailability(ActionEvent event) {
        String search = availabilityField.getText();
        if (search.isBlank())
            throw new IllegalArgumentException("Search cannot be blank.");

        LibraryBook book = library.getAvailableBookByTitle(search);

        if (book == null) {
            availabilityStatus.setText("Status: Not Available");
        } else {
            availabilityStatus.setText("Status: Available");
        }
    }

    /**
     * Saves the catalog and library to their respective files.
     */
    private void save() {
        bookCatalog.save();
        personCatalog.save();
        library.save();
    }

    /**
     * Returns the library.
     * @return the library
     */
    public Library getLibrary() {
        return library;
    }

    /**
     * Returns the book catalog.
     * @return the book catalog
     */
    public BookCatalog getBookCatalog() {
        return bookCatalog;
    }
}
