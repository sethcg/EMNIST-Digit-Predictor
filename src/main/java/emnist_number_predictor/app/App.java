package emnist_number_predictor.app;

import lombok.extern.slf4j.Slf4j;
import emnist_number_predictor.components.window.Window;
import emnist_number_predictor.components.window.Window.STYLESHEET;
import org.nd4j.common.config.ND4JSystemProperties;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Slf4j
public class App extends Application {

    // Set this to true to force the Neural Network Model to be rebuilt on launch,
    // This can take a long time depending on the EPOCH_NUM.
    private static final boolean ENABLE_DEBUG_REBUILD_MODEL_OPTION = false;

    // External Resource Directory
    public static final String DIRECTORY_NAME = ".emnist-number-predictor";
    public static final String DIRECTORY_PATH = String.format("%s\\%s", System.getProperty("user.home"), DIRECTORY_NAME);

    // Model File
    public static final String MODEL_FILE_NAME = "Model.zip";
    public static final String MODEL_PATH = String.format("%s\\%s", DIRECTORY_PATH, MODEL_FILE_NAME);

    public static boolean hasModel = false;
    public static Window window = new Window();
    public static AppController controller = new AppController();

    public static void main(String[] args) throws Exception {
        // Disable default logging for ND4J.
        System.setProperty(ND4JSystemProperties.LOG_INITIALIZATION, "false");
        launch(args); 
    }

    @Override
    public void init() throws Exception {
        hasModel = createModelFile();

        // Add .emnist-data-predictor folder to store and write to external files
        File directory = new File(DIRECTORY_PATH);
        if(directory.mkdir()) {
            log.info(String.format("Required %s folder created at Path: %s", DIRECTORY_NAME, DIRECTORY_PATH));
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        window.initStyle(StageStyle.UNDECORATED);
        window.initialize();
        window.setScene(STYLESHEET.LOADING_SCREEN);
        window.show();
    }

    private static boolean createModelFile() {
        // Debug option to force build.
        if(ENABLE_DEBUG_REBUILD_MODEL_OPTION) return false;

        File file = new File(MODEL_PATH);
        try {
            return !file.createNewFile();
        } catch (Exception exception) {
            log.error("Error creating the %s file.", MODEL_FILE_NAME);
            return false;
        }
    }

    public static void maximize() {
        if(window.isFullScreen()) {
    		window.setFullScreen(false);
    	}else {
        	window.setFullScreenExitHint("");
        	window.setFullScreen(true);
    	}
    }

    public static void minimize() {
        window.setIconified(true);
    }

    public static void close() {
        window.close();
    }

}