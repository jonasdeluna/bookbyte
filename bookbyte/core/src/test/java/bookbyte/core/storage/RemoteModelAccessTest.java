package bookbyte.core.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static bookbyte.core.storage.RemoteModelAccess.isServerRunning;

public class RemoteModelAccessTest {
    @Test
    public void testServerRunning() {
        Assertions.assertFalse(isServerRunning(URI.create("http://localhost:8080")));
    }
}
