package bookbyte.bookbinder;

import bookbyte.bookbinder.DataTransferObjects.LibraryBookReturnObject;
import bookbyte.core.book.Book;
import bookbyte.core.library.BookStatus;
import bookbyte.core.library.LibraryBook;
import bookbyte.core.person.Person;
import bookbyte.core.storage.LibraryBookSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value = {"/library", "/library/"})
public class LibraryController {
    private final Gson gson;


    public LibraryController() {

        this.gson = new GsonBuilder()
                .registerTypeAdapter(LibraryBook.class, new LibraryBookSerializer(BookBinderApplication.getBookCatalog(), BookBinderApplication.getPersonCatalog()))
                .create();
    }


    @PostMapping
    public ResponseEntity<String> createLibraryBook(@RequestBody bookbyte.bookbinder.DataTransferObjects.LibraryBook.LibraryBookCreationRequest request) {
        try {
            if (!BookBinderApplication.getBookCatalog().hasBook(request.getIsbn13())) {
                return new ResponseEntity<>("No book found with ID: " + request.getIsbn13(), HttpStatus.NOT_FOUND);
            }
            if (BookBinderApplication.getLibrary().hasBook(request.getId())) {
                return new ResponseEntity<>("There is already a book with that id", HttpStatus.CONFLICT);

            }
            Book book = BookBinderApplication.getBookCatalog().getBookByIsbn13(request.getIsbn13());
            LibraryBook libraryBook = new LibraryBook(request.getId(), book);
            BookBinderApplication.getLibrary().addBook(libraryBook);
            BookBinderApplication.getLibrary().save();
            return new ResponseEntity<>("LibraryBook created with ID: " + libraryBook.getId(), HttpStatus.CREATED);

        } catch (Exception e) {

            return new ResponseEntity<>("There was an error " + e, HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping(value = {"/overwrite", "/overwrite/"})
    public ResponseEntity<String> overwriteLibraryBooks(@RequestBody String bookJson) {
        try {
            JsonArray jsonArray = JsonParser.parseString(bookJson).getAsJsonArray();

            BookBinderApplication.getLibrary().clear();
            for (int i = 0; i < jsonArray.size(); i++) {
                LibraryBook book = gson.fromJson(jsonArray.get(i), LibraryBook.class);
                BookBinderApplication.getLibrary().addBook(book);
            }

            BookBinderApplication.getLibrary().save();

        } catch (Exception e) {
            return new ResponseEntity<>("There was an error with the request!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Book created successfully!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> upsertLibraryBook(@RequestBody bookbyte.bookbinder.DataTransferObjects.LibraryBook.LibraryBookCreationRequest request) {
        try {
            if (!BookBinderApplication.getBookCatalog().hasBook(request.getIsbn13())) {
                return new ResponseEntity<>("No book found with ISBN: " + request.getIsbn13(), HttpStatus.NOT_FOUND);
            }
            if (BookBinderApplication.getLibrary().hasBook(request.getId())) {
                Book book = BookBinderApplication.getBookCatalog().getBookByIsbn13(request.getIsbn13());
                BookBinderApplication.getLibrary().removeBook(BookBinderApplication.getLibrary().getBook(request.getId()));
                LibraryBook libraryBook = new LibraryBook(request.getId(), book);
                BookBinderApplication.getLibrary().addBook(libraryBook);
                BookBinderApplication.getLibrary().save();
                return new ResponseEntity<>("LibraryBook updated with ISBN13: " + request.getIsbn13(), HttpStatus.OK);

            } else {
                Book book = BookBinderApplication.getBookCatalog().getBookByIsbn13(request.getIsbn13());
                LibraryBook libraryBook = new LibraryBook(request.getId(), book);
                BookBinderApplication.getLibrary().addBook(libraryBook);
                BookBinderApplication.getLibrary().save();
                return new ResponseEntity<>("LibraryBook created with ID: " + libraryBook.getId(), HttpStatus.CREATED);

            }

        } catch (Exception e) {

            return new ResponseEntity<>("There was an error " + e, HttpStatus.BAD_REQUEST);

        }
    }

    @PatchMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<String> updateLibraryBook(@RequestBody bookbyte.bookbinder.DataTransferObjects.LibraryBook.LibraryBookCreationRequest request) {
        try {
            if (!BookBinderApplication.getBookCatalog().hasBook(request.getIsbn13())) {
                return new ResponseEntity<>("No book found with ISBN: " + request.getIsbn13(), HttpStatus.NOT_FOUND);
            }
            if (BookBinderApplication.getLibrary().hasBook(request.getId())) {
                Book book = BookBinderApplication.getBookCatalog().getBookByIsbn13(request.getIsbn13());
                BookBinderApplication.getLibrary().removeBook(BookBinderApplication.getLibrary().getBook(request.getId()));
                LibraryBook libraryBook = new LibraryBook(request.getId(), book);
                BookBinderApplication.getLibrary().addBook(libraryBook);
                BookBinderApplication.getLibrary().save();
                return new ResponseEntity<>("LibraryBook updated with ISBN13: " + request.getIsbn13(), HttpStatus.OK);

            } else {
                return new ResponseEntity<>("No book in the library with that ID: " + request.getId(), HttpStatus.NOT_FOUND);

            }

        } catch (Exception e) {

            return new ResponseEntity<>("There was an error " + e, HttpStatus.BAD_REQUEST);

        }
    }

    @DeleteMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<String> deleteLibraryBook(@PathVariable String id) {
        try {
            if (BookBinderApplication.getLibrary().hasBook(id)) {
                BookBinderApplication.getLibrary().removeBook(BookBinderApplication.getLibrary().getBook(id));
                BookBinderApplication.getLibrary().save();
                return new ResponseEntity<>("LibraryBook successfully removed: ", HttpStatus.OK);

            } else {
                return new ResponseEntity<>("No book in the library with that ID: " + id, HttpStatus.NOT_FOUND);

            }

        } catch (Exception e) {
            return new ResponseEntity<>("There was an error " + e, HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping
    public ResponseEntity<Collection<LibraryBookReturnObject>> getLibraryBook() {
        try {

            return new ResponseEntity<>(BookBinderApplication.getLibrary().getBooks()
                    .stream().map(book -> new LibraryBookReturnObject(
                    book.getId(),
                    book.getBook().getISBN13(),
                    book.getBorrower() == null ? null : book.getBorrower().uuid().toString(),
                    book.getDueDate() == null ? 0 : book.getDueDate().toEpochSecond(ZoneOffset.UTC)
            )).collect(Collectors.toList()), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(BookBinderApplication.getLibrary().getBooks()
                    .stream().map(book -> new LibraryBookReturnObject(
                            book.getId(),
                            book.getBook().getISBN13(),
                            book.getBorrower() == null ? null : book.getBorrower().uuid().toString(),
                            book.getDueDate() == null ? 0 : book.getDueDate().toEpochSecond(ZoneOffset.UTC)
                    )).collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = {"/{id}/", "/{id}/"})
    public ResponseEntity<LibraryBook> getLibraryBookDetail(@PathVariable String id) {
        try {

            return new ResponseEntity<>(BookBinderApplication.getLibrary().getBook(id), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(BookBinderApplication.getLibrary().getBook(id), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping(value = {"/{id}/borrow", "/{id}/borrow/"})
    public ResponseEntity<String> borrowBook(@PathVariable String id, @RequestBody bookbyte.bookbinder.DataTransferObjects.LibraryBook.LibraryBookLendingRequest request) {
        try {
            LibraryBook libraryBook = BookBinderApplication.getLibrary().getBook(id);
            Person lender = BookBinderApplication.getPersonCatalog().getPersonByUUID(request.getUuid());
            if (libraryBook == null || lender == null || request.getDays() < 0) {
                return new ResponseEntity<>("Book not found ", HttpStatus.NOT_FOUND);
            }

            if (libraryBook.getStatus() == BookStatus.BORROWED) {
                return new ResponseEntity<>("This book is not available! ", HttpStatus.CONFLICT);
            }

            libraryBook.borrow(lender, request.getDays());
            BookBinderApplication.getLibrary().save();
            return new ResponseEntity<>("Book successfully borrowed ", HttpStatus.OK);


        } catch (Exception e) {
            return new ResponseEntity<>("There was an error " + e, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = {"/{id}/return", "/{id}/return/"})
    public ResponseEntity<String> returnBook(@PathVariable String id, @RequestBody bookbyte.bookbinder.DataTransferObjects.LibraryBook.LibraryBookLendingRequest request) {
        try {
            LibraryBook libraryBook = BookBinderApplication.getLibrary().getBook(id);
            Person lender = BookBinderApplication.getPersonCatalog().getPersonByUUID(request.getUuid());
            if (libraryBook == null || lender == null || request.getDays() < 0) {
                return new ResponseEntity<>("Book not found ", HttpStatus.NOT_FOUND);
            }

            if (libraryBook.getStatus() == BookStatus.AVAILABLE) {
                return new ResponseEntity<>("This book is not borrowed! ", HttpStatus.CONFLICT);
            }
            if (libraryBook.getBorrower() != lender) {
                return new ResponseEntity<>("This book is not borrowed by this person! ", HttpStatus.UNAUTHORIZED);
            }

            libraryBook.returnBook();
            BookBinderApplication.getLibrary().save();
            return new ResponseEntity<>("Book successfully returned ", HttpStatus.OK);


        } catch (Exception e) {
            return new ResponseEntity<>("There was an error " + e, HttpStatus.BAD_REQUEST);
        }

    }

}
