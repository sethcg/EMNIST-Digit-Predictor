package emnist_number_predictor.components.prediction;

import emnist_number_predictor.components.controls.ControlBox;
import emnist_number_predictor.components.util.RowConstraint;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import java.util.ArrayList;

public class PredictionGrid extends GridPane {

	private static final String PREDICTION_GRID_DEFAULT_STYLE = "prediction-grid-default";
	private static final Insets GRID_INSET = new Insets(5,0,5,0);

	private ArrayList<PredictionRow> predictions = new ArrayList<PredictionRow>();

	public PredictionGrid() {
		this.getStyleClass().add(PREDICTION_GRID_DEFAULT_STYLE);
		this.setAlignment(Pos.CENTER);
        this.setPadding(GRID_INSET);

		this.getRowConstraints().addAll(
			new RowConstraint(VPos.CENTER, Priority.NEVER, 50), 
			new RowConstraint(VPos.CENTER, Priority.ALWAYS)
		);

		// Draggable.addDraggableListener(App.window, this);
		this.initializeGrid();
	}

	private void initializeGrid() {
		// Initialize control buttons
		this.add(new ControlBox(), 0, 0);

		// Initialize prediction rows
		for (int digit = 0; digit < 10; digit++) {
			PredictionRow row = new PredictionRow(digit);
			this.add(row, 0,  digit + 1);
			this.predictions.add(row);
		}
	}

	public void resetPredictionGrid() {
		for(PredictionRow prediction : predictions) {
			prediction.setPercent(0.0);
	    }
	}

	public void updatePredictionGrid(double[] percentages) {
		for(int digit = 0; digit < predictions.size(); digit++) {
			// Percentage represented as a double between [0-1]
			double normalizedPercent = percentages[digit];
			predictions.get(digit).setPercent(normalizedPercent);
	    }
	}

}
