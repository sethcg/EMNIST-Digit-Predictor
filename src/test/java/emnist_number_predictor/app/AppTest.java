package emnist_number_predictor.app;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import emnist_number_predictor.components.window.Window;
import javafx.application.Platform;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class AppTest {

    private App app;

    @Start
    private void start(Stage stage) {
        // Arrange
        app = new App();
        app.window = new Window();
    }

    @Test
    void testMaximize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Act
                app.maximize();

                // Assert
                assertTrue(App.window.isFullScreen());
            }
        });
    }

    @Test
    void testMinimize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Act
                app.minimize();

                // Assert
                assertTrue(App.window.isIconified());
            }
        });
    }

    @Test
    void testClose() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Act
                app.close();

                // Assert
                assertFalse(App.window.isShowing());
            }
        });
    }

}
