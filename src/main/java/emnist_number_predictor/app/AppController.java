package emnist_number_predictor.app;

import lombok.extern.slf4j.Slf4j;
import emnist_number_predictor.components.input.InputCell;
import emnist_number_predictor.components.input.InputGrid;
import emnist_number_predictor.components.prediction.PredictionGrid;
import emnist_number_predictor.service.ModelService;
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

	public final static int UPSCALE_FACTOR = 2;
    public final static int UPSCALED_GRID_SIZE = InputGrid.GRID_SIZE * UPSCALE_FACTOR;
    public final static int UPSCALED_ARRAY_SIZE = UPSCALED_GRID_SIZE * UPSCALED_GRID_SIZE;

	public static InputGrid inputGrid = new InputGrid();
	public static PredictionGrid predictionGrid = new PredictionGrid();

	private static BufferedImage screenshot = new BufferedImage(UPSCALED_GRID_SIZE, UPSCALED_GRID_SIZE, BufferedImage.TYPE_INT_RGB);
	private static float[] floatRGBArray = new float[UPSCALED_ARRAY_SIZE];

	private static INDArray getPrediction() {
		INDArray predictionInput = Nd4j.create(floatRGBArray, new int[]{1, 784});
			
		// Normalize the data from color values of [0-255] to values of [0-1]
		DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
		scalar.transform(predictionInput);

		// Get prediction from Neural Network Model
		return ModelService.getPrediction(predictionInput);
	}

	private static void updatePredictionGrid() {
		if(inputGrid.isEmpty()){
			predictionGrid.resetPredictionGrid();
		} else {
			INDArray predictionOutput = getPrediction();

			// Update prediction grid with new percentages
			double[] percentages = predictionOutput.data().asDouble();
			predictionGrid.updatePredictionGrid(percentages);
		}
	}

	public static void updatePrediction(InputCell cell) {
        // Sets the necessary values given a row/column from a 14x14 single dimensional matrix. 
        // Upscaled to 28x28 matrix, for each input cell selected.
		int colorRGB = Integer.MAX_VALUE;
        int upscaledRow = UPSCALE_FACTOR * cell.row;
        int upscaledColumn = UPSCALE_FACTOR * cell.column;

        // Set RGB float values, for INDArray (neural network) purposes.
        floatRGBArray[(upscaledRow * UPSCALED_GRID_SIZE + upscaledColumn)] = cell.colorValue;
        floatRGBArray[(upscaledRow * UPSCALED_GRID_SIZE + upscaledColumn) + 1] = cell.colorValue;
        floatRGBArray[(upscaledRow + 1) * UPSCALED_GRID_SIZE + upscaledColumn] = cell.colorValue;
        floatRGBArray[((upscaledRow + 1) * UPSCALED_GRID_SIZE + upscaledColumn) + 1] = cell.colorValue;

        // setRGB values for the current BufferedImage.
		screenshot.setRGB(upscaledColumn, upscaledRow, colorRGB);
		screenshot.setRGB(upscaledColumn, upscaledRow + 1, colorRGB);
		screenshot.setRGB(upscaledColumn + 1, upscaledRow, colorRGB);
		screenshot.setRGB(upscaledColumn + 1, upscaledRow + 1, colorRGB);

		// Update prediction displayed values.
		updatePredictionGrid();
	}

	public static void resetPrediction() {
		// Return float array and BufferedImage to their defaults
		Arrays.fill(floatRGBArray, 0);
		screenshot = new BufferedImage(UPSCALED_GRID_SIZE, UPSCALED_GRID_SIZE, BufferedImage.TYPE_INT_RGB);

		// Reset input grid
    	inputGrid.resetGrid();

		// Update prediction displayed values
		updatePredictionGrid();
	}

	public static void saveScreenshot() {
		final String SCREENSHOT_FILE_NAME = "screenshot.png";
    	final String SCREENSHOT_PATH = String.format("%s\\%s", App.DIRECTORY_PATH, SCREENSHOT_FILE_NAME);
		try {
			ImageIO.write(screenshot, "png", new File(SCREENSHOT_PATH));
		} catch (IOException e) {
			log.error("Error saving %s to ", SCREENSHOT_FILE_NAME, SCREENSHOT_PATH);
		}
    }

}
