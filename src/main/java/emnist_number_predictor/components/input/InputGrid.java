package emnist_number_predictor.components.input;

import static emnist_number_predictor.util.Const.*;

import emnist_number_predictor.app.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public final class InputGrid extends GridPane {

    private static final double GRID_MAX_WIDTH_PERCENTAGE = 0.6;
    private static final double GRID_INSET_PERCENTAGE = 0.05;

    private InputCell[] inputCells = new InputCell[GRID_SIZE * GRID_SIZE];

    public InputGrid() {
        this.setGridSize(GRID_MAX_WIDTH_PERCENTAGE, GRID_INSET_PERCENTAGE);

        // Draggable.addDraggableListener(App.window, this);
        this.initializeInputCells();

        // Adjust grid size, when the Appliction window size changes.
		App.window.width.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                setGridSize(GRID_MAX_WIDTH_PERCENTAGE, GRID_INSET_PERCENTAGE);
			}
		});
		App.window.height.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                setGridSize(GRID_MAX_WIDTH_PERCENTAGE, GRID_INSET_PERCENTAGE);
			}
		});
    }

    private void setGridSize(double maxWidthPercentage, double insetPercentage) {
        setMaxWidth(App.window.height.get() * maxWidthPercentage);
        setMaxHeight(App.window.width.get());
        setPadding(new Insets((App.window.height.get() * insetPercentage)));
    }

    private void initializeInputCells() {
        for (int row = 0; row < GRID_SIZE; row++) {
    		for (int column = 0; column < GRID_SIZE; column++) {
                inputCells[row * GRID_SIZE + column] = new InputCell(row, column);
    			this.add(inputCells[row * GRID_SIZE + column], column, row);
    		}
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

    public void resetGrid() {
        for (InputCell inputCell : inputCells) {
            inputCell.setStyle("-fx-background-color: -fx-cell-white;");
            inputCell.colorValue = 0;
        }
    }

}
