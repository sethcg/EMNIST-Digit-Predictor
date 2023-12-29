package emnist_number_predictor.components.prediction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class PredictionRowTest {

    @Start
    private void start(Stage stage) {
        App.hasModel = true;
        Window.width = new SimpleDoubleProperty(0, "_");
        Window.height = new SimpleDoubleProperty(0, "_");
    }

    @Test
    void testSetPercent() {
        // Arrange
        double normalizedPercent = 0.9;
        PredictionRow row = new PredictionRow(9);

        // Act
        row.setPercent(normalizedPercent);

        // Assert
        assertEquals(row.digitLabel.getText(), "9");
        assertEquals(row.percentLabel.getText(), "90.00%");
        assertEquals(row.progressBar.progressProperty().get(), 0.9);
    }

}
