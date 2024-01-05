package bookbyte.bookbinder;

import bookbyte.core.ValidationUtils;
import bookbyte.core.book.Book;
import bookbyte.core.storage.BookSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = {"/books", "/books/"})
public class BookController {

    private final Gson gson;

    public BookController() {

        BookSerializer bookSerializer = new BookSerializer();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Book.class, bookSerializer)
                .create();

    }

    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody String bookJson) {
        try {
            Book book = gson.fromJson(bookJson, Book.class);
            try {
                ValidationUtils.validateISBN13(book.getISBN13());
            } catch (Exception e) {
                return new ResponseEntity<>("Invalid ISBN!", HttpStatus.BAD_REQUEST);

            }
            if (BookBinderApplication.getBookCatalog().getBookByIsbn13(book.getISBN13()) != null) {
                return new ResponseEntity<>("The book already exists! Use a PUT request instead!", HttpStatus.CONFLICT);
            }


            BookBinderApplication.getBookCatalog().addBook(book);
            BookBinderApplication.getBookCatalog().save();

        } catch (Exception e) {
            return new ResponseEntity<>("There was an error with the request!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Book created successfully!", HttpStatus.CREATED);
    }

    @PostMapping(value = {"/overwrite", "/overwrite/"})
    public ResponseEntity<String> overwriteBooks(@RequestBody String bookJson) {
        try {
            JsonArray jsonArray = JsonParser.parseString(bookJson).getAsJsonArray();

            BookBinderApplication.getBookCatalog().clear();
            for (int i = 0; i < jsonArray.size(); i++) {
                Book book = gson.fromJson(jsonArray.get(i), Book.class);
                try {
                    ValidationUtils.validateISBN13(book.getISBN13());
                } catch (Exception e) {
                    return new ResponseEntity<>("Invalid ISBN!", HttpStatus.BAD_REQUEST);

                }
                BookBinderApplication.getBookCatalog().addBook(book);
            }

            BookBinderApplication.getBookCatalog().save();

        } catch (Exception e) {
            return new ResponseEntity<>("There was an error with the request!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Book created successfully!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> upsertBook(@RequestBody String bookJson) {
        HttpStatus status = HttpStatus.CONFLICT;
        try {
            Book book = gson.fromJson(bookJson, Book.class);
            Book searchBook = BookBinderApplication.getBookCatalog().getBookByIsbn13(book.getISBN13());
            if (searchBook != null) {
                BookBinderApplication.getBookCatalog().removeBook(searchBook);
                status = HttpStatus.OK;
            } else {
                status = HttpStatus.CREATED;
            }
            BookBinderApplication.getBookCatalog().addBook(book);
            BookBinderApplication.getBookCatalog().save();

        } catch (Exception e) {
            return new ResponseEntity<>("There was an error with the request!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Book updated successfully!", status);
    }

    @PatchMapping(value = {"/{isbn13}", "/{isbn13}/"})
    public ResponseEntity<String> updateBook(@RequestBody String bookJson, @PathVariable String isbn13) {
        HttpStatus status;
        try {
            Book updatedBook = gson.fromJson(bookJson, Book.class);

            if (!isbn13.equals(updatedBook.getISBN13())) {
                return new ResponseEntity<>("ISBN in the path and book data do not match.", HttpStatus.BAD_REQUEST);
            }

            Book currentBook = BookBinderApplication.getBookCatalog().getBookByIsbn13(isbn13);
            if (currentBook == null) {
                return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
            } else {
                BookBinderApplication.getBookCatalog().removeBook(currentBook);
                BookBinderApplication.getBookCatalog().addBook(updatedBook);
                BookBinderApplication.getBookCatalog().save();
                status = HttpStatus.OK;
            }
        } catch (Exception e) {
            return new ResponseEntity<>("There was an error with the request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Book updated successfully!", status);
    }

    @DeleteMapping(value = {"/{isbn13}", "/{isbn13}/"})
    public ResponseEntity<String> deleteBook(@PathVariable String isbn13) {
        try {
            Book bookToRemove = BookBinderApplication.getBookCatalog().getBookByIsbn13(isbn13);
            if (bookToRemove == null) {
                return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
            } else {
                BookBinderApplication.getBookCatalog().removeBook(bookToRemove);
                BookBinderApplication.getBookCatalog().save();
                return new ResponseEntity<>("Book deleted successfully.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("There was an error processing your request: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/{isbn13}", "/{isbn13}/"})
    public ResponseEntity<Book> getBookByIsbn13(@PathVariable String isbn13) {
        Book book = BookBinderApplication.getBookCatalog().getBookByIsbn13(isbn13);
        if (book == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(book);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> allBooks = BookBinderApplication.getBookCatalog().getCatalog();
        if (allBooks.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(allBooks, HttpStatus.OK);
        }
    }
}
