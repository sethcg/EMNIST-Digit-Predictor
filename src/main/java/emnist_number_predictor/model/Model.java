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

	private File file;
	private MultiLayerNetwork network;
	private ConfigurationProgress progress;

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

	public Model(File file, ConfigurationProgress progress) {
		this.file = file;
		this.progress = progress;

		this.network = new MultiLayerNetwork(this.getConfiguration());
		this.initializeModel(progress.hasModel);
	}

	public INDArray getPrediction(INDArray predictionInput) {
		return network.output(predictionInput);
	}

	private MultiLayerConfiguration getConfiguration() {
		this.progress.incrementProgress(PROGRESS.CONFIGURATION, "Configuring Neural Network");
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
						.nOut(EMNIST_DIGIT_OUTPUT)
						.activation(Activation.SOFTMAX)
						.weightInit(WeightInit.XAVIER)
						.build())
				.build();
	}

	private void initializeModel(boolean hasModel) {
		progress.incrementProgress(PROGRESS.CONFIGURATION, "Fetching EMNIST Data");
		try {
			this.trainingIterator = new EmnistDataSetIterator(EMNIST_SET, BATCH_SIZE, true);
			this.testingIterator = new EmnistDataSetIterator(EMNIST_SET, BATCH_SIZE, false);	
		} catch (Exception exception) {
			log.error("Error fetching EMNIST datasets.");
		}

		if(hasModel) {
			this.trainModel();
			this.evaluateModel();
			this.saveModel();
		}
		
		try {
			this.loadModel();
		} catch (Exception exception) {
			this.initializeModel(hasModel);
		}
	}

	private void saveModel() {
		progress.incrementProgress(PROGRESS.CONFIGURATION, "Saving Model");
		try {
			ModelSerializer.writeModel(network, this.file, false);
		} catch (IOException exception) {
			log.error("Error saving %s to Path: %s", MODEL_FILE_NAME, MODEL_PATH);
		}
	}

	private void loadModel() throws IOException {
		progress.incrementProgress(PROGRESS.CONFIGURATION, "Loading Model");
		try {
			network = ModelSerializer.restoreMultiLayerNetwork(this.file);
		} catch (IOException exception) {
			log.error("Error loading %s from Path: %s", MODEL_FILE_NAME, MODEL_PATH);
		}
	}

	private void trainModel() {
		System.out.println(EPOCH_NUM);
		for(int i = 1; i < EPOCH_NUM + 1; i++) {
			String configurationText = String.format("Training Epoch %d / %d", i, EPOCH_NUM);
			progress.incrementProgress(PROGRESS.TRAINING, configurationText);
			network.fit(trainingIterator);
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

	private void evaluateModel() {
		// Evaluate basic performance
		String basicEvaluation = network.evaluate(testingIterator).stats();
		progress.incrementProgress(PROGRESS.EVALUATION, "Evaluating basic performance");

		// Evaluate ROC and calculate the area under curve
		String rocEvaluation = network.evaluateROCMultiClass(testingIterator, 0).stats();
		progress.incrementProgress(PROGRESS.EVALUATION, "Evaluating ROC performance");

		try {
			File file = new File(EVALUATION_PATH);
			file.createNewFile();
			PrintWriter output = new PrintWriter(EVALUATION_PATH);
			output.println("Evaluation Information for the Neural Network Model:");
			output.printf("EPOCH_NUM: %d", EPOCH_NUM);
			output.printf("%n%s%n%n%s", basicEvaluation, rocEvaluation);
			output.flush();
			output.close();
		} catch (IOException exception) {
			log.error("Error saving %s to Path: %s", EVALUATION_FILE_NAME, EVALUATION_PATH);
		}
	}

	// private static String formatTime(long millis) {
	// 	long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
	// 	long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
	// 	return String.format("%02d:%02d", minutes, seconds - TimeUnit.MINUTES.toSeconds(minutes));
	// }

}
