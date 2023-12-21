package emnist_number_predictor.components.prediction;

import emnist_number_predictor.components.controls.ControlBox;
import emnist_number_predictor.components.util.HorizontalRow;
import emnist_number_predictor.components.util.RowConstraint;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import java.util.ArrayList;

public class PredictionGrid extends GridPane {

	private ArrayList<PredictionRow> predictions = new ArrayList<PredictionRow>();

	public PredictionGrid() {
		this.getStyleClass().add("prediction-grid");
		this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10,0,10,0));

		this.initializeGrid();
	}

	private void initializeGrid() {

		this.getRowConstraints().addAll(
			new RowConstraint(VPos.CENTER, Priority.NEVER, 50), 
			new RowConstraint(VPos.CENTER, Priority.ALWAYS)
		);

		// Initialize control buttons
		this.addRow(0, new ControlBox());

		// Initialize prediction rows
		for (int digit = 0; digit < 10; digit++) {
			PredictionRow row = new PredictionRow(digit);
			this.predictions.add(row);

			HorizontalRow container = new HorizontalRow(0, Pos.CENTER, row.digitLabel, row.percentLabel, row.progressBar);	
			this.addRow(digit + 1, container);
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
