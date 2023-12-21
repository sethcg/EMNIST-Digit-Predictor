package emnist_number_predictor.components.input;

import emnist_number_predictor.app.AppController;
import emnist_number_predictor.components.window.Window;
import emnist_number_predictor.util.HandleMouse;
import emnist_number_predictor.util.Listener;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class InputCell extends Region {

    private static final float WHITE_COLOR_VALUE = 255;
	private static final float BLACK_COLOR_VALUE = 0;
    private static final Double INIT_CELL_WIDTH = Window.INIT_WIDTH / InputGrid.GRID_SIZE;
	private static final Double INIT_CELL_HEIGHT = Window.INIT_HEIGHT / InputGrid.GRID_SIZE;

	private ObjectProperty<Double> cellWidth = new SimpleObjectProperty<Double>(this, "cell-width", INIT_CELL_WIDTH);
	private ObjectProperty<Double> cellHeight = new SimpleObjectProperty<Double>(this, "cell-height", INIT_CELL_HEIGHT);

	public int row, column;
	public boolean selected;
	public float colorValue = BLACK_COLOR_VALUE;

    public InputCell(int row, int column) {
		this.getStyleClass().add("input-cell");
		this.row = row;
		this.column = column;

		// Initialize cell size. 
		this.prefWidthProperty().bind(cellWidth);
		this.prefHeightProperty().bind(cellHeight);

		// Adjust cell size, when the Appliction window size changes.
		Window.width.addListener(new Listener<Number>(() -> { cellWidth.setValue(Window.width.get() / (double) InputGrid.GRID_SIZE); }));
		Window.height.addListener(new Listener<Number>(() -> { cellWidth.setValue(Window.height.get() / (double) InputGrid.GRID_SIZE); }));

		// Handle MouseClick
		this.setOnMouseClicked(new HandleMouse<MouseEvent>((event) -> { updateColor(event); }));
		this.setOnMousePressed(new HandleMouse<MouseEvent>((event) -> { updateColor(event); }));

		// Handle MouseDrag
		this.setOnDragDetected(new HandleMouse<MouseEvent>((event) -> { startFullDrag(); }));
		this.setOnMouseDragEntered(new HandleMouse<MouseEvent>((event) -> { updateColor(event); }));
    }

	public void select() {
		this.selected = true;
		this.setStyle("-fx-background-color: -fx-cell-black;");
		this.colorValue = WHITE_COLOR_VALUE;
	}

	public void deselect() {
		this.selected = false;
		this.setStyle("-fx-background-color: -fx-cell-white;");
		this.colorValue = BLACK_COLOR_VALUE;
	}

	private void updateColor(MouseEvent event) {
    	InputCell inputCell = (InputCell) event.getSource();
		if(event.isPrimaryButtonDown() && !inputCell.selected) { 
			// Right MouseButton select
			this.select();
			AppController.updatePrediction(inputCell);
    	} else if(event.isSecondaryButtonDown() && inputCell.selected) { 
			// Right MouseButton deselect
			this.deselect();
			AppController.updatePrediction(inputCell);
    	}
    }

}
