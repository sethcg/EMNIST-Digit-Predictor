package emnist_number_predictor.components.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class InputCellTest {

    @Start
    private void start(Stage stage) {
        App.hasModel = true;
        Window.width = new SimpleDoubleProperty(0, "_");
        Window.height = new SimpleDoubleProperty(0, "_");
    }

    @Test
    void testDeselect() {
        // Arrange
        final float BLACK_COLOR_VALUE = 0;
        final InputCell cell = new InputCell(0, 0);

        // Act
        cell.deselect();

        // Assert
        assertFalse(cell.selected);
        assertTrue(cell.getStyle().contains("-fx-cell-white"));
        assertEquals(cell.colorValue, BLACK_COLOR_VALUE);
    }

    @Test
    void testSelect() {
        // Arrange
        final float WHITE_COLOR_VALUE = 255;
        final InputCell cell = new InputCell(0, 0);

        // Act
        cell.select();

        // Assert
        assertTrue(cell.selected);
        assertTrue(cell.getStyle().contains("-fx-cell-black"));
        assertEquals(cell.colorValue, WHITE_COLOR_VALUE);
    }

}
