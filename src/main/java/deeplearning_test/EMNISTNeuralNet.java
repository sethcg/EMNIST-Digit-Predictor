package deeplearning_test;

import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;

import org.nd4j.evaluation.classification.*;

import org.nd4j.linalg.learning.config.Adam; // for different updaters like Adam, Nesterovs, etc.
import org.nd4j.linalg.activations.Activation; // defines different activation functions like RELU, SOFTMAX, etc.
import org.nd4j.linalg.lossfunctions.LossFunctions; // mean squared error, multiclass cross entropy, etc.


public class EMNISTNeuralNet {
	
	public static void main(String[] args) throws Exception {
		
		int batchSize = 128; 	// how many examples to simultaneously train in the network
		EmnistDataSetIterator.Set emnistSet = EmnistDataSetIterator.Set.BALANCED;
		EmnistDataSetIterator emnistTrain = new EmnistDataSetIterator(emnistSet, batchSize, true);
		EmnistDataSetIterator emnistTest = new EmnistDataSetIterator(emnistSet, batchSize, false);
	
		int outputNum = EmnistDataSetIterator.numLabels(emnistSet); // total output classes
		long rngSeed = 123; 	// integer for reproducibility of a random number generator
		int numRows = 28; 		// number of "pixel rows" in an MNIST digit
		int numColumns = 28;

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
		
			
		// create the MLN
		MultiLayerNetwork network = new MultiLayerNetwork(conf);
		network.init();

		// pass a training listener that reports score every 10 iterations
		int eachIterations = 10;
		network.addListeners(new ScoreIterationListener(eachIterations));

		// or simply use for loop
		int numEpochs = 2;
		for(int i = 0; i < numEpochs; i++) {
			System.out.print("Epoch " + i + " / " + numEpochs);
		   network.fit(emnistTrain);
		}
		
		// evaluate basic performance
		Evaluation eval = network.evaluate(emnistTest);
		//System.out.println(eval.accuracy());
		//System.out.println(eval.precision());
		//System.out.println(eval.recall());

		// evaluate ROC and calculate the Area Under Curve
		ROCMultiClass roc = network.evaluateROCMultiClass(emnistTest, 0);
		//System.out.println(roc.calculateAverageAUC());

		// calculate AUC for a single class
        //int classIndex = 0;
        //System.out.println(roc.calculateAUC(classIndex));


		// optionally, you can print all stats from the evaluations
		System.out.print(eval.stats());
		System.out.print(roc.stats());
	}
	
}
