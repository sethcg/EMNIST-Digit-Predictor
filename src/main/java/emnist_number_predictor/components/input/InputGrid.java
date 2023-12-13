package emnist_number_predictor.components.input;
import static emnist_number_predictor.util.Const.*;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window;
import emnist_number_predictor.util.Draggable;
import emnist_number_predictor.util.Listener;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public final class InputGrid extends BorderPane {

    // private static final double GRID_MAX_WIDTH_PERCENTAGE = 0.6;
    // private static final double GRID_INSET_PERCENTAGE = 0.05;

    private InputCell[] inputCells = new InputCell[GRID_SIZE * GRID_SIZE];

    public InputGrid() {
        // Draggable.addDraggableListener(App.window, this);

        // Adjust grid size, when the Appliction window size changes.
		Window.width.addListener(new Listener<Number>(() -> { setSize(); }));
		Window.height.addListener(new Listener<Number>(() -> { setSize(); }));

        this.setCenter(this.initializeGrid());
        this.setBorder(new Border(new BorderStroke(Color.RED, 
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }
    
    private void setSize() {
        var windowHeight = Window.height.get();
        var windowWidth = Window.width.get();
        this.setMaxWidth(Math.min(windowHeight, windowWidth));
        this.setMaxHeight(Math.min(windowHeight, windowWidth));
    }

    private GridPane initializeGrid() {
        GridPane inputGrid = new GridPane();
        for (int row = 0; row < GRID_SIZE; row++) {
    		for (int column = 0; column < GRID_SIZE; column++) {
                inputCells[row * GRID_SIZE + column] = new InputCell(row, column);
    			inputGrid.add(inputCells[row * GRID_SIZE + column], column, row);
    		}
    	}
        return inputGrid;
    }

    public boolean isEmpty() {
        for (InputCell inputCell : inputCells) {
            if(inputCell.selected) {
                return false;
            }
        }
        return true;
    }

    public void resetGrid() {
        for (InputCell inputCell : inputCells) {
            inputCell.deselect();
        }
    }

}
