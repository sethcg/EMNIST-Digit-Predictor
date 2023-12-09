package emnist_number_predictor.components.input;
import static emnist_number_predictor.util.Const.*;

import emnist_number_predictor.app.App;
// import emnist_number_predictor.components.window.Window;
// import emnist_number_predictor.util.Draggable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public final class InputGrid extends GridPane {

    private InputCell[] inputCells = new InputCell[GRID_SIZE * GRID_SIZE];

    public InputGrid() {
        // Draggable.addDraggableListener(App.window, this);

        // Initialize grid size.
        this.setWidth(INIT_WINDOW_WIDTH);
		this.setHeight(INIT_WINDOW_HEIGHT);
        this.setMaxWidth(INIT_WINDOW_HEIGHT * 0.6);
        this.setMaxHeight(INIT_WINDOW_WIDTH);
        this.setPadding(new Insets((INIT_WINDOW_HEIGHT * 0.05)));

        // Initialize cells to the input grid.
        for (int row = 0; row < GRID_SIZE; row++) {
    		for (int column = 0; column < GRID_SIZE; column++) {
    			InputCell inputCell = new InputCell(row, column);
                inputCells[row * GRID_SIZE + column] = inputCell;
    			this.add(inputCell, column, row);
    		}
    	}

        // Adjust input grid size, when the Appliction window width or height changes.
		App.window.width.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                setGridSize();
			}
		});
		App.window.height.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                setGridSize();
			}
		});
    }

    public void resetGrid() {
        for (InputCell inputCell : inputCells) {
            inputCell.setStyle("-fx-background-color: -fx-cell-white;");
            inputCell.colorValue = 0;
        }
    }

    public boolean isEmpty() {
        for (InputCell inputCell : inputCells) {
            if(inputCell.colorValue > 0) {
                return false;
            }
        }
        return true;
    }

    private void setGridSize() {
        setMaxWidth(App.window.height.get() * 0.6);
        setMaxHeight(App.window.width.get());
        setPadding(new Insets((App.window.height.get() * 0.05)));
    }

}
