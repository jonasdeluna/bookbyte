module bookbyte.ui {

    requires java.net.http;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires atlantafx.base;

    requires bookbyte.core;

    opens bookbyte.ui to javafx.graphics, javafx.fxml;
}
