package emnist_number_predictor.service;
import static emnist_number_predictor.util.Const.*;

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

import emnist_number_predictor.service.ModelConfigurationProgress.PROGRESS;

@Slf4j
public class ModelService {

	private static File file = new File(MODEL_PATH);
	private static MultiLayerNetwork network;

	private static final long RNG_SEED = 123;
	private static final int EMNIST_IMAGE_WIDTH = 28;
	private static final int EMNIST_IMAGE_HEIGHT = 28;
	private static final int LAYER_ONE_INPUT = EMNIST_IMAGE_WIDTH * EMNIST_IMAGE_HEIGHT;
	private static final int LAYER_ONE_OUTPUT = 1000;

	// Final output of EMNIST labels digits [0-9]
	private static final int EMNIST_DIGIT_OUTPUT = 10;

	private static final int BATCH_SIZE = 128;
	private static final EMnistSet EMNIST_SET = EMnistSet.DIGITS;
	private static EmnistDataSetIterator trainingIterator, testingIterator;

	public static INDArray getPrediction(INDArray predictionInput) {
		return network.output(predictionInput);
	}

	public static void initializeModel(boolean hasModel) {
		ModelConfigurationProgress.initialize();
		if(hasModel) {
			try {
				loadModel();
			} catch (IOException exception) {
				// On loading error, build new model.
				ModelConfigurationProgress.initialize();
				getData();
				MultiLayerConfiguration configuration = getConfiguration();
				MultiLayerNetwork newNetwork = new MultiLayerNetwork(configuration);
				trainModel(newNetwork);
				evaluateModel(newNetwork);
				saveModel(newNetwork);
			}
		} else {
			getData();
			MultiLayerConfiguration configuration = getConfiguration();
			MultiLayerNetwork newNetwork = new MultiLayerNetwork(configuration);
			trainModel(newNetwork);
			evaluateModel(newNetwork);
			saveModel(newNetwork);
			network = newNetwork;
		}
	}

	private static MultiLayerConfiguration getConfiguration() {
		ModelConfigurationProgress.setConfigurationText("Configuring Neural Network");
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
		ModelConfigurationProgress.setConfigurationProgress(PROGRESS.CONFIGURATION);
		return configuration;
	}

	private static void getData() {
		ModelConfigurationProgress.setConfigurationText("Fetching EMNIST Datasets");
		try {
			trainingIterator = new EmnistDataSetIterator(EMNIST_SET, BATCH_SIZE, true);
			testingIterator = new EmnistDataSetIterator(EMNIST_SET, BATCH_SIZE, false);
			ModelConfigurationProgress.setConfigurationProgress(PROGRESS.CONFIGURATION);
		} catch (Exception exception) {
			log.error("Error Fetching EMNIST Datasets.");
		}
	}

	private static void saveModel(MultiLayerNetwork network) {
		ModelConfigurationProgress.setConfigurationText("Saving Model");
		try {
			ModelSerializer.writeModel(network, file, false);
			ModelConfigurationProgress.setConfigurationProgress(PROGRESS.CONFIGURATION);
		} catch (IOException exception) {
			log.error("Error Saving %s To Path: %s", MODEL_FILE_NAME, MODEL_PATH);
		}
	}

	private static void loadModel() throws IOException {
		ModelConfigurationProgress.setConfigurationText("Loading Model");
		try {
			network = ModelSerializer.restoreMultiLayerNetwork(file);
			ModelConfigurationProgress.setConfigurationProgress(PROGRESS.CONFIGURATION);
		} catch (IOException exception) {
			log.error("Error Loading %s From Path: %s", MODEL_FILE_NAME, MODEL_PATH);
			throw exception;
		}
	}

	private static void trainModel(MultiLayerNetwork network) {
		for(int i = 1; i < EPOCH_NUM + 1; i++) {
			String configurationText = String.format("Training Epoch %d / %d", i, EPOCH_NUM);
			ModelConfigurationProgress.setConfigurationText(configurationText);
			network.fit(trainingIterator);
			ModelConfigurationProgress.setConfigurationProgress(PROGRESS.TRAINING);
		}
	}

	private static void evaluateModel(MultiLayerNetwork network) {
		ModelConfigurationProgress.setConfigurationText("Evaluating basic performance");
		// Evaluate basic performance
		String basicEvaluation = network.evaluate(testingIterator).stats();
		ModelConfigurationProgress.setConfigurationProgress(PROGRESS.EVALUATION);

		// Evaluate ROC and calculate the area under curve
		ModelConfigurationProgress.setConfigurationText("Evaluating ROC performance");
		String rocEvaluation = network.evaluateROCMultiClass(testingIterator, 0).stats();
		ModelConfigurationProgress.setConfigurationProgress(PROGRESS.EVALUATION);

		try {
			ModelConfigurationProgress.setConfigurationText("Saving Evaluation");
			File file = new File(EVALUATION_PATH);
			file.createNewFile();
			PrintWriter output = new PrintWriter(EVALUATION_PATH);
			output.println("Evaluation Information for the Neural Network Model:");
			output.printf("EPOCH_NUM: %d", EPOCH_NUM);
			output.printf("%n%s%n%n%s", basicEvaluation, rocEvaluation);
			output.flush();
			output.close();
			ModelConfigurationProgress.setConfigurationProgress(PROGRESS.EVALUATION);
		} catch (IOException exception) {
			log.error("Error saving %s to Path: %s", EVALUATION_FILE_NAME, EVALUATION_PATH);
		}
	}

}
