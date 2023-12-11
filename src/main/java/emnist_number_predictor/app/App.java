package emnist_number_predictor.app;
import static emnist_number_predictor.util.Const.*;

import lombok.extern.slf4j.Slf4j;
import emnist_number_predictor.components.window.LoadingService;
import emnist_number_predictor.components.window.Window;
import emnist_number_predictor.components.window.Window.STYLESHEET;
import org.nd4j.common.config.ND4JSystemProperties;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

@Slf4j
public class App extends Application {

    private static BorderPane root = new BorderPane();

    public static Window window = new Window(root);
    public static AppController controller = new AppController();

    public static void main(String[] args) throws Exception {
        // Disable default logging for ND4J.
        System.setProperty(ND4JSystemProperties.LOG_INITIALIZATION, "false");
        launch(args); 
    }

    @Override
    public void init() throws Exception {
        // Add .emnist-data-predictor folder to store and write to external files
        File directory = new File(DIRECTORY_PATH);
        if(directory.mkdir()) {
            log.info(String.format("Required %s folder created at Path: %s", DIRECTORY_NAME, DIRECTORY_PATH));
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize Window Values
        window.setTitle("EMNIST Number Predictor");
        // window.initStyle(StageStyle.UNDECORATED);

        LoadingService.showLoadingScreen(window);
    }
    
    public static void showApplication(Window window) {
        Scene scene = new Scene(root, INIT_WINDOW_WIDTH, INIT_WINDOW_HEIGHT);
        scene.setUserAgentStylesheet(STYLESHEET.APPLICATION.getPath());

    	root.prefHeightProperty().bind(scene.heightProperty());
    	root.prefWidthProperty().bind(scene.widthProperty());

        // Initializing the Application services.
        root.setCenter(controller.inputGrid);
        root.setBottom(controller.predictionGrid);
        controller.resetPrediction();

        window.setScene(scene);
        window.show();
    }

}