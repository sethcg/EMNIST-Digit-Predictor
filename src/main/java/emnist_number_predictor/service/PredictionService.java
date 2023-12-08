package emnist_number_predictor.service;

import emnist_number_predictor.components.InputCell;
import emnist_number_predictor.components.PredictionGrid;
import java.io.IOException;
import java.util.Arrays; 
import org.nd4j.linalg.api.ndarray.INDArray;

public class PredictionService {

	public PredictionGrid predictionGrid;

	public PredictionService(AppService app) throws IOException {
		this.predictionGrid = new PredictionGrid(app);
		this.runPrediction();
	}

	public void runPrediction() {
		GridService gridService = AppService.getGridService();
		if(gridService.inputGrid.isEmpty()){
			predictionGrid.resetPredictionGrid();
		} else {
			INDArray predictionInput = gridService.getINDArray();
			predictionGrid.updatePredictionGrid(predictionInput);
		}
	}

	public void updatePrediction(InputCell inputCell) {
		GridService gridService = AppService.getGridService();
		gridService.updateINDArray(inputCell.row, inputCell.column, inputCell.colorValue);
		this.runPrediction();
	}

	public void resetPrediction() {
		GridService gridService = AppService.getGridService();
		Arrays.fill(gridService.integerRGBArray, 0);
		Arrays.fill(gridService.floatRGBArray, 0);
        gridService.inputGrid.resetGrid();
		this.runPrediction();
	}

}
