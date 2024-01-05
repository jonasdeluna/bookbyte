module bookbyte.bookbinder {
    requires com.google.gson;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    requires bookbyte.core;
    requires spring.webmvc;

    exports bookbyte.bookbinder;
    exports bookbyte.bookbinder.DataTransferObjects;
    opens bookbyte.bookbinder to spring.core, spring.beans, spring.context, org.springframework.boot, org.springframework.boot.autoconfigure, org.springframework.boot.test.mock.mockito;
}
