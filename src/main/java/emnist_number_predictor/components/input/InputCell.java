package emnist_number_predictor.components.input;
import static emnist_number_predictor.util.Const.*;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window;
import emnist_number_predictor.util.HandleMouse;
import emnist_number_predictor.util.Listener;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class InputCell extends StackPane {

	public int row, column;
	public boolean selected;

	public float colorValue = BLACK_COLOR_VALUE;
	private static final String INPUT_CELL_DEFAULT_STYLE = "input-cell-default";

	private ObjectProperty<Double> cellWidth = new SimpleObjectProperty<Double>(this, "cell-width", INIT_CELL_WIDTH);
	private ObjectProperty<Double> cellHeight = new SimpleObjectProperty<Double>(this, "cell-height", INIT_CELL_HEIGHT);

    public InputCell(int row, int column) {
		this.getStyleClass().add(INPUT_CELL_DEFAULT_STYLE);
		this.row = row;
		this.column = column;

		// Initialize cell size. 
		this.prefWidthProperty().bind(cellWidth);
		this.prefHeightProperty().bind(cellHeight);

		// Adjust cell size, when the Appliction window size changes.
		Window.width.addListener(new Listener<Number>(() -> { cellWidth.setValue(Window.width.get() / (double) GRID_SIZE); }));
		Window.height.addListener(new Listener<Number>(() -> { cellWidth.setValue(Window.height.get() / (double) GRID_SIZE); }));

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
			App.controller.updatePrediction(inputCell);
    	} else if(event.isSecondaryButtonDown() && inputCell.selected) { 
			// Right MouseButton deselect
			this.deselect();
			App.controller.updatePrediction(inputCell);
    	}
    }

}
