package emnist_number_predictor.components.input;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class InputGridTest {

    @Start
    private void start(Stage stage) {
        App.hasModel = true;
        Window.width = new SimpleDoubleProperty(0, "_");
        Window.height = new SimpleDoubleProperty(0, "_");
    }

    @Test
    void testIsEmpty_True() {
        // Arrange
        InputGrid grid = new InputGrid();

        // Act
        boolean result = grid.isEmpty();

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsEmpty_False() {
        // Arrange
        InputGrid grid = new InputGrid();
        GridPane container = (GridPane) grid.getChildren().get(0);
        InputCell inputCell = (InputCell) container.getChildren().get(0);
        inputCell.select();

        // Act
        boolean result = grid.isEmpty();

        // Assert
        assertFalse(result);
    }

    @Test
    void testResetGrid() {
        // Arrange
        InputGrid grid = new InputGrid();
        GridPane container = (GridPane) grid.getChildren().get(0);
        InputCell inputCell = (InputCell) container.getChildren().get(0);
        inputCell.select();

        // Act
        grid.resetGrid();

        // Assert
        assertFalse(inputCell.selected);
        assertTrue(grid.isEmpty());
    }

}
