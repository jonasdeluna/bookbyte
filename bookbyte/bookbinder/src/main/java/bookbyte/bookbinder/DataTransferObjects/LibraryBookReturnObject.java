package bookbyte.bookbinder.DataTransferObjects;

public record LibraryBookReturnObject(String id, String isbn13, String borrower, long dueDate) {

}