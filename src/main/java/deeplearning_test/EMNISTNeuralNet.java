package deeplearning_test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import javax.imageio.ImageIO;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.evaluation.classification.ROCMultiClass;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;


public class EMNISTNeuralNet{

	static MultiLayerNetwork network;
	
	// For Creating a model (But only used to update/change saved model)
	// Accuracy: 0.9856 of current model
	/*
	public static void main(String[] args) throws Exception{
		createModel();
	}
	*/
	
	// Save the Grid as a PNG file
	protected static void createData(int array[][]){
		String path = "./tempImage.png";
	    BufferedImage image = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_BINARY);
	    for (int x = 0; x < 28; x++){
	        for (int y = 0; y < 28; y++){
	        	image.setRGB(x, y, Color.BLACK.getRGB());
	        	if(x >= 4 && y >= 4 && x < 24 && y < 24){
	        		if(array[x - 4][y - 4] == 1){
	        			image.setRGB(x, y, Color.WHITE.getRGB());
	        		}
	        	}
	    	}
	    }
	    
	    File ImageFile = new File(path);
	    try{
	        ImageIO.write(image, "png", ImageFile);
	    }catch (IOException e){
	        e.printStackTrace();
	    }
	}
	
	// Called when application is run to load model located in ./Model
	protected static void loadModel() throws IOException{
		network = MultiLayerNetwork.load(new File("./Model.zip"), true);
	}

	
	// Method to get predictions of the tempImage
	protected static ArrayList<Double> getPredictions() throws IOException{
		int height = 28;
		int width = 28;
		int channels = 1;
		
        // Load the File
        File file = new File("./tempImage.png");
        
        //Use the nativeImageLoader to convert to numerical matrix
        NativeImageLoader loader = new NativeImageLoader(height, width, channels);
        
        // Put image in INDArray
        INDArray image = loader.asMatrix(file);
        
        // *IMPORTANT* - Reshape to correct Matrix size for MNIST data
        INDArray reshaped = image.reshape(1,784);
        
        // Scale Images 
        DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
        scalar.transform(reshaped);
        
        //System.out.println(image.toString());

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
	

	
	public static void createModel() throws Exception {
		
		int batchSize = 128; 	// how many examples to simultaneously train in the network
		EmnistDataSetIterator.Set emnistSet = EmnistDataSetIterator.Set.DIGITS;
		EmnistDataSetIterator emnistTrain = new EmnistDataSetIterator(emnistSet, batchSize, true);
		EmnistDataSetIterator emnistTest = new EmnistDataSetIterator(emnistSet, batchSize, false);
	
		int outputNum = EmnistDataSetIterator.numLabels(emnistSet); 	// total output classes
		long rngSeed = 123; 	// integer for reproducibility of a random number generator
		int numRows = 28; 		// number of "pixel rows" in an MNIST digit
		int numColumns = 28;	// number of "pixel columns" in an MNIST digit

		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.seed(rngSeed)
				.updater(new Adam())
				.l2(1e-4)
				.list()
				.layer(new DenseLayer.Builder()
						.nIn(numRows * numColumns) 		// Number of input data points.
						.nOut(1000) 					// Number of output data points.
						.activation(Activation.RELU) 	// Activation function.
						.weightInit(WeightInit.XAVIER) 	// Weight initialization.
						.build())
				.layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
						.nIn(1000)
						.nOut(outputNum)
						.activation(Activation.SOFTMAX)
						.weightInit(WeightInit.XAVIER)
						.build())
				.build();
		
			
		// Create the MLN
		MultiLayerNetwork network = new MultiLayerNetwork(conf);
		network.init();

		// Training listener that reports score every 100 iterations
		int eachIterations = 100;
		network.addListeners(new CustomScoreIterationListener(eachIterations));

		// Loop through creating the each Epoch
		int numEpochs = 2;
		network.fit(emnistTrain, numEpochs);
		
		// Evaluate basic performance
		Evaluation eval = network.evaluate(emnistTest);

		// Evaluate ROC and calculate the Area Under Curve
		ROCMultiClass roc = network.evaluateROCMultiClass(emnistTest, 0);
		//System.out.println(roc.calculateAverageAUC());

		// Statistics from the evaluations
		System.out.print(eval.stats());
		System.out.print(roc.stats());
		
		// Save the Model to a File
		network.save(new File("./Model.zip"));
	}
	
}
