package emnist_number_predictor.app;
import static emnist_number_predictor.util.Const.*;

import lombok.extern.slf4j.Slf4j;
import emnist_number_predictor.components.input.InputCell;
import emnist_number_predictor.components.input.InputGrid;
import emnist_number_predictor.components.prediction.PredictionGrid;
import emnist_number_predictor.model.Model;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;

@Slf4j
public class AppController {

	private Model model;

	public InputGrid inputGrid = new InputGrid();
	public PredictionGrid predictionGrid = new PredictionGrid();

    private int[] integerRGBArray = new int[UPSCALED_ARRAY_SIZE];
    private float[] floatRGBArray = new float[UPSCALED_ARRAY_SIZE];

	// Expensive method, loads or creates the Neural Network Model.
	// Both take time, and this is called within scope of the loading screen.
	public void initializeModel() {
		this.model = new Model();
	}

	private INDArray getPrediction() {
		INDArray predictionInput = Nd4j.create(floatRGBArray, new int[]{1, 784});
			
		// Normalize the data from color values of [0-255] to values of [0-1]
		DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
		scalar.transform(predictionInput);

		// Get prediction from Neural Network Model
		return model.getPrediction(predictionInput);
	}

	private void updatePredictionGrid() {
		if(inputGrid.isEmpty()){
			this.predictionGrid.resetPredictionGrid();
		} else {
			INDArray predictionOutput = this.getPrediction();

			// Update prediction grid with new percentages
			double[] percentages = predictionOutput.data().asDouble();
			this.predictionGrid.updatePredictionGrid(percentages);
		}
	}

	public void updatePrediction(InputCell cell) {
        // Sets the necessary values given a row/column from a 14x14 single dimensional matrix. 
        // Upscaled to 28x28 matrix, for each input cell selected.
		int colorValue = cell.colorValue;
        int upscaledRow = UPSCALE_FACTOR * cell.row;
        int upscaledColumn = UPSCALE_FACTOR * cell.column;

        // Set RGB float values, for INDArray (neural network) purposes.
        floatRGBArray[(upscaledRow * UPSCALED_GRID_SIZE + upscaledColumn)] = (float) colorValue;
        floatRGBArray[(upscaledRow * UPSCALED_GRID_SIZE + upscaledColumn) + 1] = (float) colorValue;
        floatRGBArray[(upscaledRow + 1) * UPSCALED_GRID_SIZE + upscaledColumn] = (float) colorValue;
        floatRGBArray[((upscaledRow + 1) * UPSCALED_GRID_SIZE + upscaledColumn) + 1] = (float) colorValue;

        // Set RGB int values, for saving image purposes.
        integerRGBArray[upscaledRow * UPSCALED_GRID_SIZE + upscaledColumn] = colorValue;
        integerRGBArray[(upscaledRow * UPSCALED_GRID_SIZE + upscaledColumn) + 1] = colorValue;
        integerRGBArray[(upscaledRow + 1) * UPSCALED_GRID_SIZE + upscaledColumn] = colorValue;
        integerRGBArray[((upscaledRow + 1) * UPSCALED_GRID_SIZE + upscaledColumn) + 1] = colorValue;

		// Update prediction displayed values.
		this.updatePredictionGrid();
	}

	public void resetPrediction() {
		// Fill RGB arrays with default values.
		Arrays.fill(integerRGBArray, 0);
		Arrays.fill(floatRGBArray, 0);

		// Reset input grid.
        this.inputGrid.resetGrid();

		// Update prediction displayed values.
		this.updatePredictionGrid();
	}

	public void saveScreenshot() {
        BufferedImage screenshot = new BufferedImage(UPSCALED_GRID_SIZE, UPSCALED_GRID_SIZE, BufferedImage.TYPE_INT_RGB);

        // Set RGB values for each pixel of the 28x28 image based on the rgbArray.
        screenshot.setRGB(0, 0, UPSCALED_GRID_SIZE, UPSCALED_GRID_SIZE, integerRGBArray, 0, UPSCALED_GRID_SIZE);

		try {
			ImageIO.write(screenshot, "png", new File(SCREENSHOT_PATH));
		} catch (IOException e) {
			log.error("Error saving %s to ", SCREENSHOT_FILE_NAME, SCREENSHOT_PATH);
		}
    }

}
