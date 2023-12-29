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
public class PredictionGridTest {

    @Start
    private void start(Stage stage) {
        App.hasModel = true;
        Window.width = new SimpleDoubleProperty(0, "_");
        Window.height = new SimpleDoubleProperty(0, "_");
    }
    
    @Test
    void testResetPredictionGrid() {
        // Arrange
        PredictionGrid grid = new PredictionGrid();
        grid.updatePredictionGrid(new double[]{ 0.9, 0.1, 0, 0, 0, 0, 0, 0, 0, 0});

        // Act
        grid.resetPredictionGrid();

        // Assert
        assertEquals(grid.predictions.get(0).digitLabel.getText(), "0");
        assertEquals(grid.predictions.get(0).progressBar.progressProperty().get(), 0);
        assertEquals(grid.predictions.get(0).percentLabel.getText(), "0.00%");
    }

    @Test
    void testUpdatePredictionGrid() {
        // Arrange
        PredictionGrid grid = new PredictionGrid();

        // Act
        grid.updatePredictionGrid(new double[]{ 0.9, 0.1, 0, 0, 0, 0, 0, 0, 0, 0});

        // Assert
        assertEquals(grid.predictions.get(0).digitLabel.getText(), "0");
        assertEquals(grid.predictions.get(0).progressBar.progressProperty().get(), 0.9);
        assertEquals(grid.predictions.get(0).percentLabel.getText(), "90.00%");
    }

}
