package emnist_number_predictor.components.window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window.STYLESHEET;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class WindowTest {
    private App app;

    @Start
    private void start(Stage stage) {
        // Arrange
        app = new App();
        app.window.initialize();
    }

    @Test
    void testSetScene_Application() {
        // Act
        app.window.setScene(STYLESHEET.APPLICATION);
        Scene scene = (Scene) app.window.sceneProperty().getValue();
        BorderPane root = (BorderPane) scene.getRoot();
        String[] path = scene.userAgentStylesheetProperty().get().split("/");

        // Assert
        assertEquals(path[path.length - 1], "app.css");
        assertEquals(root.topProperty().get().getClass().getSimpleName(), "WindowHeader");
        assertEquals(root.centerProperty().get().getClass().getSimpleName(), "InputGrid");
        assertEquals(root.bottomProperty().get().getClass().getSimpleName(), "PredictionGrid");
    }

    @Test
    void testSetScene_LoadingScreen() {
        // Act
        app.window.setScene(STYLESHEET.LOADING_SCREEN);
        Scene scene = (Scene) app.window.sceneProperty().getValue();
        BorderPane root = (BorderPane) scene.getRoot();
        String[] path = scene.userAgentStylesheetProperty().get().split("/");

        // Assert
        assertEquals(path[path.length - 1], "loading-screen.css");
        assertEquals(root.centerProperty().get().getClass().getSimpleName(), "LoadingScreen");
    }

}
