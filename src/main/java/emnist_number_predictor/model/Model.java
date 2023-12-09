package emnist_number_predictor.model;
import static emnist_number_predictor.util.Const.*;

import emnist_number_predictor.model.ConfigurationProgress.PROGRESS;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.eclipse.deeplearning4j.resources.utils.EMnistSet;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

@Slf4j
public class Model {

	private MultiLayerNetwork network;
	private ConfigurationProgress progress = new ConfigurationProgress();

	private static final long RNG_SEED = 123;
	private static final int EMNIST_IMAGE_WIDTH = 28;
	private static final int EMNIST_IMAGE_HEIGHT = 28;
	private static final int LAYER_ONE_INPUT = EMNIST_IMAGE_WIDTH * EMNIST_IMAGE_HEIGHT;
	private static final int LAYER_ONE_OUTPUT = 1000;

	// Final output of EMNIST labels digits [0-9]
	private static final int EMNIST_DIGIT_OUTPUT = 10;

	private static final int BATCH_SIZE = 128;
	private static final EMnistSet EMNIST_SET = EMnistSet.DIGITS;
	private EmnistDataSetIterator trainingIterator, testingIterator;

	public Model() {
		this.network = new MultiLayerNetwork(this.getConfiguration());
		this.initializeModel();
	}

	public INDArray getPrediction(INDArray predictionInput) {
		return network.output(predictionInput);
	}

	private MultiLayerConfiguration getConfiguration() {
		log.info("Configuring Neural Network Model");
		MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
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
						.nOut(EMNIST_DIGIT_OUTPUT)
						.activation(Activation.SOFTMAX)
						.weightInit(WeightInit.XAVIER)
						.build())
				.build();
		progress.incrementProgress(PROGRESS.CONFIGURATION);
		return configuration;
	}

	private void initializeModel() {
		log.info("Initializing Neural Network Model");
		try {
			this.trainingIterator = new EmnistDataSetIterator(EMNIST_SET, BATCH_SIZE, true);
			this.testingIterator = new EmnistDataSetIterator(EMNIST_SET, BATCH_SIZE, false);
			progress.incrementProgress(PROGRESS.CONFIGURATION);

			File file = new File(MODEL_PATH);
			if(file.createNewFile() || DEBUG_REBUILD_MODEL_OPTION) {
				this.trainModel();
				this.evaluateModel();
				this.saveModel();
			}
			progress.incrementProgress(PROGRESS.CONFIGURATION);
			this.loadModel();
			progress.incrementProgress(PROGRESS.CONFIGURATION);
		} catch (Exception exception) {
			log.error("Error during the initialization process.");
		}
	}

	private void saveModel() throws Exception {
		log.info("Saving the model");
		try {
			File modelFile = new File(MODEL_PATH);
			ModelSerializer.writeModel(network, modelFile, false);
		} catch (Exception exception) {
			log.error("Error saving %s to Path: %s", MODEL_FILE_NAME, MODEL_PATH);
			throw exception;
		}
	}

	private void loadModel() throws IOException {
		log.info("Loading the model");
		File modelFile = new File(MODEL_PATH);
		try {
			network = ModelSerializer.restoreMultiLayerNetwork(modelFile);
			System.out.println(network);
		} catch (IOException exception) {
			log.error("Error loading %s from Path: %s", MODEL_FILE_NAME, MODEL_PATH);
			throw exception;
		}
	}

	private void trainModel() {
		log.info("Training the model through %s epochs.", EPOCH_NUM);
		for(int i = 1; i < EPOCH_NUM + 1; i++) {
			network.fit(trainingIterator);
			progress.incrementProgress(PROGRESS.TRAINING);
		}
	}

	// public void debugTraining() {
	// 	log.info("Training the model through %s epochs.", EPOCH_NUM);
	// 	long startTotal = System.currentTimeMillis();
	// 	for(int i = 0; i < EPOCH_NUM; i++) {
	// 		long start = System.currentTimeMillis();
	// 		network.fit(trainingIterator);
	// 		long end = System.currentTimeMillis();
	// 		long total = end - start;
	// 		log.info("Epoch #%d finished in %d seconds.", i, (int) TimeUnit.MILLISECONDS.toSeconds(total));
	// 	}
	// 	long endTotal = System.currentTimeMillis();
	// 	log.info("Total training time: " + formatTime(endTotal - startTotal));
	// }

	private void evaluateModel() throws Exception {
		log.info("Evaluating the model's performance.");
		// Evaluate basic performance
		String basicEvaluation = network.evaluate(testingIterator).stats();
		progress.incrementProgress(PROGRESS.EVALUATION);

		// Evaluate ROC and calculate the area under curve
		String rocEvaluation = network.evaluateROCMultiClass(testingIterator, 0).stats();
		progress.incrementProgress(PROGRESS.EVALUATION);

		try {
			File file = new File(EVALUATION_PATH);
			file.createNewFile();
			PrintWriter output = new PrintWriter(EVALUATION_PATH);
			output.println("Evaluation Information for the Neural Network Model:");
			output.printf("EPOCH_NUM: %d", EPOCH_NUM);
			output.printf("%n%s%n%n%s", basicEvaluation, rocEvaluation);
			output.flush();
			output.close();
		} catch (Exception exception) {
			log.error("Error evaluating %s to Path: %s", EVALUATION_FILE_NAME, EVALUATION_PATH);
			throw exception;
		}
	}

	// private static String formatTime(long millis) {
	// 	long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
	// 	long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
	// 	return String.format("%02d:%02d", minutes, seconds - TimeUnit.MINUTES.toSeconds(minutes));
	// }

}
