package bookbyte.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationUtilsTest {

    @Test
    public void testValidEmail() {

        String email = "john@doe.com";

        Assertions.assertDoesNotThrow(() -> ValidationUtils.validateEmail(email), "Email should be valid");
    }

    @Test
    public void testInvalidEmail() {

        String email = "ssdflskdj";

        Assertions.assertThrows(IllegalArgumentException.class, () -> ValidationUtils.validateEmail(email), "Email should be invalid");
    }

    @Test
    public void validateInvalidISBN() {

        String isbn = "1";

        Assertions.assertThrows(IllegalArgumentException.class, () -> ValidationUtils.validateISBN13(isbn), "ISBN should be invalid");
    }

    @Test
    public void validateInvalidSerialNumber() {

        String serialNumber = "1";

        Assertions.assertThrows(IllegalArgumentException.class, () -> ValidationUtils.validateBookID(serialNumber), "Serial number should be invalid");
    }
}
