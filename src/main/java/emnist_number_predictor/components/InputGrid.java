package emnist_number_predictor.components;

import emnist_number_predictor.service.AppService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public final class InputGrid extends GridPane {

    public final static int GRID_SIZE = 14;
    public final static int UPSCALED_GRID_SIZE = 28;
    public final static int UPSCALED_ARRAY_SIZE = UPSCALED_GRID_SIZE * UPSCALED_GRID_SIZE;

    private ObjectProperty<Double> windowWidth = new SimpleObjectProperty<Double>(this, "windowWidth", null);
	private ObjectProperty<Double> windowHeight = new SimpleObjectProperty<Double>(this, "windowHeight", null);

    private static InputCell[] inputCells = new InputCell[GRID_SIZE * GRID_SIZE];

    public InputGrid(AppService app) {
        // Initialize grid size.
        this.setWidth(AppService.INIT_WIDTH);
		this.setHeight(AppService.INIT_HEIGHT);

        // Bind window size variables locally. 
        windowWidth.bind(app.windowWidth);
        windowHeight.bind(app.windowHeight);
        this.setGridSize();

        // Initialize cells to the input grid.
        for (int row = 0; row < GRID_SIZE; row++) {
    		for (int column = 0; column < GRID_SIZE; column++) {
    			InputCell inputCell = new InputCell(app, row, column);
                inputCells[row * GRID_SIZE + column] = inputCell;
    			this.add(inputCell, column, row);
    		}
    	}

        // Adjust input grid size, when the Appliction window width or height changes.
		windowWidth.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				setGridSize();
			}
		});
		windowHeight.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				setGridSize();
			}
		});
    }

    public void resetGrid() {
        // Set colors to their default.
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

    public void setGridSize() {
        setMaxWidth(windowHeight.get() * 0.6);
        setMaxHeight(windowWidth.get());
        setPadding(new Insets((windowHeight.get() * 0.05)));
    }

}
