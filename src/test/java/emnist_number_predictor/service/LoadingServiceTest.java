package emnist_number_predictor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class LoadingServiceTest {
    
    @Start
    private void start(Stage stage) {
        App.hasModel = true;
        Window.width = new SimpleDoubleProperty(0, "_");
        Window.height = new SimpleDoubleProperty(0, "_");
    }

    public static void waitForRunLater() throws Exception {
        // Allow the asserts to run after all other runLater tasks complete.
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> semaphore.release());
        semaphore.acquire();
    }

    @Test
    void testSetConfigurationProgress() throws Exception {
        // Arrange
        double expected = 0.9;
        
        // Act
        LoadingService.setConfigurationProgress(expected);
        waitForRunLater();

        // Assert
        assertEquals(expected, LoadingService.configurationProgress.get());
    }

    @Test
    void testSetConfigurationText()  throws Exception {
        // Arrange
        String expected = "90.00%";

        // Act
        LoadingService.setConfigurationText(expected);
        waitForRunLater();

        // Assert
        assertEquals(expected, LoadingService.configurationProgressText.get());
    }

}
