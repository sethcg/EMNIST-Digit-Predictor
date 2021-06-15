package emnist_digit_predictor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

public class PredictionHelper {
	
	private static MultiLayerNetwork network;
	
	// Called to load model located in ./Model
	protected static void loadModel() throws IOException{
		network = MultiLayerNetwork.load(new File("./Model.zip"), true);
	}

	// Method to get predictions of the tempImage
	protected static ArrayList<Double> getPredictions(File file) throws IOException{
		int height = 28;
		int width = 28;
		int channels = 1;
	        
		//Use the nativeImageLoader to convert to numerical matrix
		NativeImageLoader loader = new NativeImageLoader(height, width, channels);
	        
		// Put image in INDArray
		INDArray image = loader.asMatrix(file);
	        
		// *IMPORTANT* - Reshape to correct Matrix size for MNIST data
		INDArray reshaped = image.reshape(1,784);
	        
		// Scale Images 
		DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
		scalar.transform(reshaped);

		// Try the *reshaped* array in the Model
		INDArray output = network.output(reshaped, false);

		// Print Data
		ArrayList<Double> predictions = new ArrayList<Double>();
		for(int i = 0; i < 10; i++){
	        double percentage = Math.round((output.getDouble(i) * 100.0) * 100.0) / 100.0;
	        predictions.add(percentage);
	    }
		return predictions;
	}
	
}
