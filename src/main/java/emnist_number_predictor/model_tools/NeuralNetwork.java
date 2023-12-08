package emnist_number_predictor.model_tools;

import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.eclipse.deeplearning4j.resources.utils.EMnistSet;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.evaluation.classification.ROCMultiClass;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

@Slf4j
public class NeuralNetwork extends MultiLayerNetwork {

    private static final int BATCH_SIZE = 128;
	private static final EMnistSet EMNIST_SET = EMnistSet.DIGITS;
	private static final long RNG_SEED = 123;

	// LAYER ONE:
	private static final int EMNIST_IMAGE_WIDTH = 28;
	private static final int EMNIST_IMAGE_HEIGHT = 28;
	private static final int LAYER_ONE_INPUT = EMNIST_IMAGE_WIDTH * EMNIST_IMAGE_HEIGHT;
	private static final int LAYER_ONE_OUTPUT = 1000;

	// LAYER TWO:
	// Final output of EMNIST labels digits [0-9]
	private static final int LAYER_TWO_OUTPUT = 10;

	public EmnistDataSetIterator trainingIterator, testingIterator;

    public NeuralNetwork() throws IOException {
        super(getConfiguration());
		try {
			this.trainingIterator = new EmnistDataSetIterator(EMNIST_SET, BATCH_SIZE, true);
			this.testingIterator = new EmnistDataSetIterator(EMNIST_SET, BATCH_SIZE, false);
		} catch (IOException exception) {
			log.error("Error in retrieving EMNIST data sets.", exception);
			throw exception;
		}
    }
    
    private static MultiLayerConfiguration getConfiguration() {
		return new NeuralNetConfiguration.Builder()
				.seed(RNG_SEED)
				.updater(new Adam())
				.l2(1e-4)
				.list()
				.layer(new DenseLayer.Builder()
						.nIn(LAYER_ONE_INPUT)
						.nOut(LAYER_ONE_OUTPUT)
						.activation(Activation.RELU)
						.weightInit(WeightInit.XAVIER)
						.build())
				.layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
						.nIn(LAYER_ONE_OUTPUT)
						.nOut(LAYER_TWO_OUTPUT)
						.activation(Activation.SOFTMAX)
						.weightInit(WeightInit.XAVIER)
						.build())
				.build();
	}

	public void train(int epochNum) {
		long startTotal = System.currentTimeMillis();
		for(int i = 0; i < epochNum; i++) {
			long start = System.currentTimeMillis();
			this.fit(trainingIterator);
			long end = System.currentTimeMillis();
			long total = end - start;
			log.debug("Epoch #" + i + " " + TimeUnit.MILLISECONDS.toSeconds(total) + " seconds");
		}
		long endTotal = System.currentTimeMillis();
		log.debug("Total time: " + formatTime(endTotal - startTotal));
	}

	public void evaluate() {
		// Evaluate basic performance.
		Evaluation eval = this.evaluate(testingIterator);

		// Evaluate ROC and calculate the Area Under Curve
		ROCMultiClass roc = this.evaluateROCMultiClass(testingIterator, 0);

		// Print Evaluation results.
		log.debug(eval.stats());
		log.debug(roc.stats());
	}

	public void save(String folderName, String fileName) throws Exception {
		try {
			String relativePath = getClass().getClassLoader().getResource(folderName).getFile() + fileName;
			File file = new File(relativePath);
			this.save(file, false);
		} catch (Exception exception) {
			log.error(String.format("Error while saving the model %s.", fileName));
			throw exception;
		}
	}

	private static String formatTime(long millis) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		return String.format("%02d:%02d", minutes, seconds - TimeUnit.MINUTES.toSeconds(minutes));
	}

}
