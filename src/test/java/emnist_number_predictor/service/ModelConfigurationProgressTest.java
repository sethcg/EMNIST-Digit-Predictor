package emnist_number_predictor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window;
import emnist_number_predictor.service.ModelConfigurationProgress.PROGRESS;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class ModelConfigurationProgressTest {

    @Start
    private void start(Stage stage) {
        // Arrange
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
        ModelConfigurationProgress.initialize(10);
        LoadingService.configurationProgress.set(0);
        double configurationExpected = Math.round((0.2 / 4) * 100) / 100.0;
        double trainingExpected = Math.round(((0.7 / 10) + configurationExpected) * 100) / 100.0;
        double evaluationExpected = Math.round(((0.1 / 3) + trainingExpected) * 100) / 100.0;

        // Act
        ModelConfigurationProgress.setConfigurationProgress(PROGRESS.CONFIGURATION);
        waitForRunLater();
        double configurationResult = Math.round(LoadingService.configurationProgress.get() * 100) / 100.0;

        ModelConfigurationProgress.setConfigurationProgress(PROGRESS.TRAINING);
        waitForRunLater();
        double trainingResult = Math.round(LoadingService.configurationProgress.get() * 100) / 100.0;
        
        ModelConfigurationProgress.setConfigurationProgress(PROGRESS.EVALUATION);
        waitForRunLater();
        double evaluationResult = Math.round(LoadingService.configurationProgress.get() * 100) / 100.0;

        // Assert
        assertEquals(configurationExpected, configurationResult);
        assertEquals(trainingExpected, trainingResult);
        assertEquals(evaluationExpected, evaluationResult);
    }

    @Test
    void testSetConfigurationText() throws Exception {
        // Arrange
        String expected = "90.00%";
        ModelConfigurationProgress configurationProgress = new ModelConfigurationProgress();

        // Act
        configurationProgress.setConfigurationText(expected);
        waitForRunLater();

        // Assert
        assertEquals(expected, LoadingService.configurationProgressText.get());
    }

}
