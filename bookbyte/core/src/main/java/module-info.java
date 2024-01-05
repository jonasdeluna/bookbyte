module bookbyte.core {

    requires java.net.http;
    requires com.google.gson;

    exports bookbyte.core;
    exports bookbyte.core.person;
    exports bookbyte.core.book;
    exports bookbyte.core.library;
    exports bookbyte.core.storage;
    opens bookbyte.core.book to com.google.gson;

}
