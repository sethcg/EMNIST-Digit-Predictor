package emnist_number_predictor.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nd4j.linalg.factory.Nd4j;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import emnist_number_predictor.components.input.InputCell;
import emnist_number_predictor.components.window.Window;
import emnist_number_predictor.service.ModelService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
@ExtendWith(MockitoExtension.class)
public class AppControllerTest {

@Start
    private void start(Stage stage) {
        App.hasModel = true;
        Window.width = new SimpleDoubleProperty(0, "_");
        Window.height = new SimpleDoubleProperty(0, "_");
    }

    @Test
    void testResetPrediction() {
        // Arrange
        AppController controller = new AppController();
        InputCell inputCell = new InputCell(0, 0);
        inputCell.colorValue = 0;
        controller.updatePrediction(inputCell);

        // Act
        controller.resetPrediction();

        // Assert
        assertTrue(controller.inputGrid.isEmpty());
        assertEquals(controller.predictionGrid.predictions.size(), 10);
        assertEquals(controller.predictionGrid.predictions.get(0).digitLabel.getText(), "0");
        assertEquals(controller.predictionGrid.predictions.get(0).percentLabel.getText(), "0.00%");
        assertEquals(controller.predictionGrid.predictions.get(0).progressBar.progressProperty().get(), 0);
    }

    @Test
    void testUpdatePrediction() {
        // Arrange
        AppController controller = new AppController();
        GridPane gridContainer = (GridPane) controller.inputGrid.getChildren().get(0);
        InputCell inputCell = (InputCell) gridContainer.getChildren().get(0);
        inputCell.select();

        float[] mockedOutput = { 
            (float) 0.0320, (float) 0.5617, (float) 0.0589, (float) 0.0183, (float) 0.0186, 
            (float) 0.1796, (float) 0.0185, (float) 0.0728, (float) 0.0133, (float) 0.0264 };
        Mockito.mockStatic(ModelService.class);
        Mockito.when(ModelService.getPrediction(any())).thenReturn(Nd4j.create(mockedOutput, new int[]{1, 10}));

        // Act
        controller.updatePrediction(inputCell);

        // Assert
        assertFalse(controller.inputGrid.isEmpty());
        assertEquals(controller.predictionGrid.predictions.size(), 10);
        assertEquals(controller.predictionGrid.predictions.get(0).digitLabel.getText(), "0");
        assertNotEquals(controller.predictionGrid.predictions.get(0).percentLabel.getText(), "0.00%");
        assertNotEquals(controller.predictionGrid.predictions.get(0).progressBar.progressProperty().get(), 0);

    }

}
