package emnist_number_predictor.service;

import emnist_number_predictor.components.InputGrid;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;

public class GridService {
    
    private final static int UPSCALE_FACTOR = 2;

	public InputGrid inputGrid;
    public int[] integerRGBArray = new int[InputGrid.UPSCALED_ARRAY_SIZE];
    public float[] floatRGBArray = new float[InputGrid.UPSCALED_ARRAY_SIZE];

    public GridService(AppService app) {
        this.inputGrid = new InputGrid(app);
    }

	public void updateINDArray(int row, int column, int color) {
        // Sets the necessary values given a row/column from a 14x14 single dimensional matrix. 
        // Upscaled to 28x28 matrix, for each input cell selected.
        int upscaledRow = UPSCALE_FACTOR * row;
        int upscaledColumn = UPSCALE_FACTOR * column;

        // Set RGB float values, for INDArray (neural network) purposes.
        this.floatRGBArray[(upscaledRow * InputGrid.UPSCALED_GRID_SIZE + upscaledColumn)] = (float) color;
        this.floatRGBArray[(upscaledRow * InputGrid.UPSCALED_GRID_SIZE + upscaledColumn) + 1] = (float) color;
        this.floatRGBArray[(upscaledRow + 1) * InputGrid.UPSCALED_GRID_SIZE + upscaledColumn] = (float) color;
        this.floatRGBArray[((upscaledRow + 1) * InputGrid.UPSCALED_GRID_SIZE + upscaledColumn) + 1] = (float) color;

        // Set RGB int values, for saving image purposes.
        this.integerRGBArray[upscaledRow * InputGrid.UPSCALED_GRID_SIZE + upscaledColumn] = color;
        this.integerRGBArray[(upscaledRow * InputGrid.UPSCALED_GRID_SIZE + upscaledColumn) + 1] = color;
        this.integerRGBArray[(upscaledRow + 1) * InputGrid.UPSCALED_GRID_SIZE + upscaledColumn] = color;
        this.integerRGBArray[((upscaledRow + 1) * InputGrid.UPSCALED_GRID_SIZE + upscaledColumn) + 1] = color;
    }

    public INDArray getINDArray() {
        INDArray inputArray = Nd4j.create(floatRGBArray, new int[]{1, 784});
        
        // Normalize the data from color values of [0-255] to values of [0-1]
        DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
		scalar.transform(inputArray);

        return inputArray;
    }

	public void saveImage() {
        BufferedImage gridImage = new BufferedImage(InputGrid.UPSCALED_GRID_SIZE, InputGrid.UPSCALED_GRID_SIZE, BufferedImage.TYPE_INT_RGB);
        // Set RGB values for each pixel of the 28x28 image based on the rgbArray.
        gridImage.setRGB(0, 0, InputGrid.UPSCALED_GRID_SIZE, InputGrid.UPSCALED_GRID_SIZE, this.integerRGBArray, 0, InputGrid.UPSCALED_GRID_SIZE);
		try {
			ImageIO.write(gridImage, "png", new File(GridService.class.getResource("/").getPath() + "screenshot.png"));
		} catch (IOException e) {
			System.out.println("Error saving prediction image file.");
		}
    }

}
