package bookbyte.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ValidationUtils {

    private static final Pattern ISBN13_PATTERN = Pattern.compile("^\\d{13}$");
    private static final Pattern BOOK_ID_PATTERN = Pattern.compile("^\\d{16}$");

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\." +
    "[a-zA-Z0-9_+&*-]+)*@" +
    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
    "A-Z]{2,7}$");


    /**
     * Provides utility methods for various validations.
     * Uninstantiable, has only staticmethods.
     */

    private ValidationUtils() {
    }

    /**
     * Validates the given ISBN13 string.
     * 
     * The method checks if the provided ISBN13 string consists only of numbers and
     * is exactly 13 characters long.
     *
     * @param isbn13 the ISBN13 string to validate.
     * @throws IllegalArgumentException if the provided isbn13 does not match the
     *                                  required format.
     */

    public static void validateISBN13(String isbn13) {
        isbn13 = isbn13.replaceAll("-", ""); 
        Matcher matcher = ISBN13_PATTERN.matcher(isbn13);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Invalid ISBN13 value. It should contain only numbers and be 13 characters long.");
        }
    }

    /**
     * Validates the given Library Book ID.
     * The method checks if the provided ID string consists only of numbers and
     * is exactly 16 characters long.
     *
     * @param bookID the Library Book ID string to validate.
     * @throws IllegalArgumentException if the provided bookID does not match the
     *                                  required format.
     */
    public static void validateBookID(String bookID) {
        Matcher matcher = BOOK_ID_PATTERN.matcher(bookID);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Invalid Library Book ID. It should contain only numbers and be 16 characters long.");
        }
    }

    /**
     * Validates the given email string.
     * The method checks if the provided email string is a valid email address.
     *
     * @param email the email string to validate.
     * @throws IllegalArgumentException if the provided email does not match the
     *                                  required format.
     */
    public static void validateEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Invalid email address.");
        }
    }
}
