package bookbyte.ui;

import bookbyte.core.book.Book;
import bookbyte.core.library.BookStatus;
import bookbyte.core.library.LibraryBook;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;


class BookByteAppTest extends ApplicationTest {

    @BeforeAll
    public static void setupHeadless() {
        BookByteApp.supportHeadless();
    }

    private BookByteController controller;

    @Override
    public void start(Stage stage) throws Exception {


        final FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("BookByte.fxml"));
        final Parent mainNode;
        try {
            mainNode = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Platform.setImplicitExit(false);

        this.controller = fxmlLoader.getController();


        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void addBook() {
        // Clear the book catalog and library before each test
        Platform.runLater(() -> {
            this.controller.getBookCatalog().clear();
            this.controller.getLibrary().clear();
            this.controller.updateBookLists();
        });

        // Add a sample book to the catalog
        clickOn("#idField");
        write("1234567890123456");
        clickOn("#isbnField");
        write("1234567891234");
        clickOn("#nameField");
        write("Sample Title");
        clickOn("#authorField");
        write("Author Name");

        clickOn("#addBookButton");
    }


    @Test
    public void testAddBook() {

        // Verify that the sample book was added to the catalog
        Assertions.assertEquals(1, controller.getLibrary().getBooks().size());
        Assertions.assertEquals(1, controller.getBookCatalog().getCatalog().size());

        Book book = controller.getBookCatalog().getCatalog().get(0);
        Assertions.assertEquals("1234567891234", book.getISBN13());
        Assertions.assertEquals("Sample Title", book.getTitle());
        Assertions.assertEquals("Author Name", book.getAuthor());
    }

    private void borrowBook() {
        clickOn("#borrowTabButton");
        clickOn("#personNameField");
        write("John Doe");
        clickOn("#emailField");
        write("john@doe.com");

        Node bookNode = lookup(".list-cell").nth(0).query();
        Point2D point = bookNode.localToScreen(bookNode.getLayoutBounds().getMinX(), bookNode.getLayoutBounds().getMinY());
        point = point.add(5, 5); // offset to get to the center of the cell (hopefully)
        clickOn(point.getX(), point.getY());

        clickOn("#borrowButton");
    }

    @Test
    public void testBorrowBook() {

        borrowBook();

        // Verify that the book was borrowed
        LibraryBook book = controller.getLibrary().getBooks().iterator().next();
        Assertions.assertEquals("John Doe", book.getBorrower().name());
        Assertions.assertEquals("john@doe.com", book.getBorrower().email());
        Assertions.assertEquals(BookStatus.BORROWED, book.getStatus());
    }

    @Test
    public void testReturnBook() {

        borrowBook();

        clickOn("#returnTabButton");

        Node bookNode = lookup(".list-cell").nth(0).query();
        Point2D point = bookNode.localToScreen(bookNode.getLayoutBounds().getMinX(), bookNode.getLayoutBounds().getMinY());
        point = point.add(5, 5); // offset to get to the center of the cell (hopefully)
        clickOn(point.getX(), point.getY());

        clickOn("#returnBookButton");

        // Verify that the book was returned
        LibraryBook book = controller.getLibrary().getBooks().iterator().next();
        Assertions.assertNull(book.getBorrower());
        Assertions.assertEquals(BookStatus.AVAILABLE, book.getStatus());
    }
}