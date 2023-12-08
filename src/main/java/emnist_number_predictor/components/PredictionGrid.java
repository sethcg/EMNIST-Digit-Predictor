package emnist_number_predictor.components;

import lombok.extern.slf4j.Slf4j;
import emnist_number_predictor.service.AppService;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class PredictionGrid extends GridPane {

	private static final String PREDICTION_GRID_DEFAULT_STYLE = "prediction-grid-default";

	public MultiLayerNetwork multiLayerNetwork;
	private ArrayList<PredictionRow> predictions = new ArrayList<PredictionRow>();

	public PredictionGrid(AppService app) throws IOException {		
		this.getStyleClass().add(PREDICTION_GRID_DEFAULT_STYLE);
		this.setPadding(new Insets(0,0,5,0));
		this.setAlignment(Pos.CENTER);
		this.setRowConstraints();

		// Initialize model.
		this.loadModel();

		// Initialize control buttons
		this.add(new ControlBar(), 0, 0);

		// Initialize prediction rows
		for (int digit = 0; digit < 10; digit++) {
			PredictionRow row = new PredictionRow(digit);
			this.add(row, 0,  digit + 1);
			this.predictions.add(row);
		}
	}

	public void loadModel() throws IOException {
		File file = new File(getClass().getClassLoader().getResource("model/Model.zip").getFile());
		try {
			this.multiLayerNetwork = MultiLayerNetwork.load(file, false);
		} catch (IOException exception) {
			log.error("Error loading neural network Model.", exception);
			throw exception;
		}
	}

	public void resetPredictionGrid() {
		for(PredictionRow prediction : predictions) {
			prediction.setPercent(0.0);
	    }
	}

	public void updatePredictionGrid(INDArray input) {
		INDArray output = multiLayerNetwork.output(input, false);
		for(int digit = 0; digit < predictions.size(); digit++) {
			// Percentage represented as a double between [0-1]
			double normalizedPercent = output.getDouble(digit);
			predictions.get(digit).setPercent(normalizedPercent);
	    }
	}

	public void setRowConstraints() {
		RowConstraints controlsRowConstraints = new RowConstraints();
		controlsRowConstraints.setMinHeight(50);
	    controlsRowConstraints.setVgrow(Priority.NEVER);
	    controlsRowConstraints.setValignment(VPos.CENTER);

	    RowConstraints predictionGridConstraints = new RowConstraints();
	    predictionGridConstraints.setVgrow(Priority.ALWAYS);
	    predictionGridConstraints.setValignment(VPos.CENTER);

	    this.getRowConstraints().addAll(controlsRowConstraints, predictionGridConstraints);
	}

}
