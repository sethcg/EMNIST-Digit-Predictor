package emnist_number_predictor;

import java.io.File;

import org.eclipse.deeplearning4j.resources.utils.EMnistSet;
import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.evaluation.classification.ROCMultiClass;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class MLN_Helper{

	// For Creating a model (But only used to update/change saved model)
	//public static void main(String[] args) throws Exception{
	//	createModel();
	//}

	public static void createModel() throws Exception {
		
		int batchSize = 128;
		EMnistSet emnistSet = EMnistSet.DIGITS;
		EmnistDataSetIterator emnistTrain = new EmnistDataSetIterator(emnistSet, batchSize, true);
		EmnistDataSetIterator emnistTest = new EmnistDataSetIterator(emnistSet, batchSize, false);
	
		int outputNum = EmnistDataSetIterator.numLabels(emnistSet); 	// Total output classes (0 - 9)
		long rngSeed = 123; 	// Integer for reproducibility of a random number generator
		int numRows = 28; 		// Number of rows in an MNIST style file
		int numColumns = 28;	// Number of columns in an MNIST style file

		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.seed(rngSeed)
				.updater(new Adam())
				.l2(1e-4)
				.list()
				.layer(new DenseLayer.Builder()
						.nIn(numRows * numColumns) 		// Number of input data points.
						.nOut(1000) 				// Number of output data points.
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
		network.addListeners(new ScoreIterationListener(eachIterations));

		// Loop through creating the each Epoch
		int numEpochs = 2;
		network.fit(emnistTrain, numEpochs);
		
		// Evaluate basic performance
		Evaluation eval = network.evaluate(emnistTest);

		// Evaluate ROC and calculate the Area Under Curve
		ROCMultiClass roc = network.evaluateROCMultiClass(emnistTest, 0);

		// Statistics from the evaluations
		System.out.print(eval.stats());
		System.out.print(roc.stats());
		
		// Save the Model to a File
		network.save(new File("../resources/Model.zip"));
	}
	
}
