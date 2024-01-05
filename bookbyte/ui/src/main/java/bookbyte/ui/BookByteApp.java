package bookbyte.ui;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class BookByteApp extends Application {

    /**
     * Helper method used by tests needing to run headless.
     */
    public static void supportHeadless() {

        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("BookByte.fxml"));
        Parent parent = fxmlLoader.load();


        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/bookbyte/images/bookbyte.jpg")));
        stage.setScene(new Scene(parent));
        stage.setTitle("BookByte (Alpha)");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
