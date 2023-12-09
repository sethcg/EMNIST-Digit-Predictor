package emnist_number_predictor.util;

public final class Const {

    // Set this to true to force the Neural Network Model to be rebuilt.
    public static final boolean DEBUG_REBUILD_MODEL_OPTION = false;

    // Window size
    public static final Double INIT_WINDOW_WIDTH = 400.0;
    public static final Double INIT_WINDOW_HEIGHT = 600.0;

    // InputGrid size
    public final static int GRID_SIZE = 14;
	public final static int UPSCALE_FACTOR = 2;
    public final static int UPSCALED_GRID_SIZE = GRID_SIZE * UPSCALE_FACTOR;
    public final static int UPSCALED_ARRAY_SIZE = UPSCALED_GRID_SIZE * UPSCALED_GRID_SIZE;

    // Cell size
    public static final Double INIT_CELL_WIDTH = INIT_WINDOW_WIDTH / GRID_SIZE;
	public static final Double INIT_CELL_HEIGHT = INIT_WINDOW_HEIGHT / GRID_SIZE;

    // External Resource Directory
    public static final String DIRECTORY_NAME = ".emnist-number-predictor";
    public static final String DIRECTORY_PATH = String.format("%s\\%s", System.getProperty("user.home"), DIRECTORY_NAME);

    // Model File
    public static final String MODEL_FILE_NAME = "Model.zip";
    public static final String MODEL_PATH = String.format("%s\\%s", DIRECTORY_PATH, MODEL_FILE_NAME);

    // Evaluation File
    public static final boolean ENABLE_EVALUATION = true;
    public static final String EVALUATION_FILE_NAME = "evaluation.txt";
    public static final String EVALUATION_PATH = String.format("%s\\%s", DIRECTORY_PATH, EVALUATION_FILE_NAME);

    // Screenshot File
    public static final String SCREENSHOT_FILE_NAME = "screenshot.png";
    public static final String SCREENSHOT_PATH = String.format("%s\\%s", DIRECTORY_PATH, SCREENSHOT_FILE_NAME);
    
}
