package emnist_number_predictor.app;
import static emnist_number_predictor.util.Const.*;

import lombok.extern.slf4j.Slf4j;
import emnist_number_predictor.components.window.Window;
import emnist_number_predictor.components.window.Window.STYLESHEET;
import org.nd4j.common.config.ND4JSystemProperties;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;

@Slf4j
public class App extends Application {

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

}