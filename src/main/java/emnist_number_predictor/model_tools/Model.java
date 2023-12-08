package emnist_number_predictor.model_tools;

import lombok.extern.slf4j.Slf4j;
import org.nd4j.common.config.ND4JSystemProperties;

@Slf4j
public class Model {

	private final static int EPOCH_NUM = 2; //11;
	private final static String FILE_NAME = "Model.zip";
	private final static String FOLDER_NAME = "/model";

	public static void main(String[] args) throws Exception {
		// Disable default logging for ND4J.
        System.setProperty(ND4JSystemProperties.LOG_INITIALIZATION, "false");

		buildModel();
	}

	public static void buildModel() throws Exception {
		NeuralNetwork model = new NeuralNetwork();

		log.info(String.format("Training the model through %s epochs.", EPOCH_NUM));
		model.train(EPOCH_NUM);

		log.info("Evaluating the model.");
		model.evaluate();

		log.info(String.format("Saving the model to %s.", FILE_NAME));
		model.save(FOLDER_NAME, FILE_NAME);
	}
	
}
