package emnist_number_predictor.app;
import static emnist_number_predictor.util.Const.*;

import lombok.extern.slf4j.Slf4j;
import emnist_number_predictor.components.window.Window;
import org.nd4j.common.config.ND4JSystemProperties;
import java.io.File;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

@Slf4j
public class App extends Application {

    private static BorderPane root = new BorderPane();
    private LoadingScreen loadingScreen = new LoadingScreen();

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
        // window.initStyle(StageStyle.UNDECORATED);
        window.setTitle("EMNIST Number Predictor");

        // Show loading screen, while also saving/loading the Model
        final Task<Void> modelTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                controller.initializeModel();
                return null;
            }
        };
        
        this.showLoadingScreen(window, modelTask);
        new Thread(modelTask).start();
    }

    private void showLoadingScreen(Window window, Task<?> task) {
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadingScreen.loadProgress.progressProperty().unbind();
                loadingScreen.loadProgress.setProgress(1);
                window.toFront();

                FadeTransition loadingTransition = new FadeTransition(Duration.seconds(1.0), new LoadingScreen());
                loadingTransition.setFromValue(1.0);
                loadingTransition.setToValue(0.0);
                loadingTransition.setOnFinished(actionEvent -> showApplication(window));
                loadingTransition.play();
            }
        });

        Scene loadingScene = new Scene(loadingScreen, INIT_WINDOW_WIDTH, INIT_WINDOW_HEIGHT, Color.TRANSPARENT);
        loadingScreen.prefHeightProperty().bind(loadingScene.heightProperty());
    	loadingScreen.prefWidthProperty().bind(loadingScene.widthProperty());

        window.setScene(loadingScene);
        window.show();
    }

    private void showApplication(Window window) {
        Scene scene = new Scene(root, INIT_WINDOW_WIDTH, INIT_WINDOW_HEIGHT);
    	root.prefHeightProperty().bind(scene.heightProperty());
    	root.prefWidthProperty().bind(scene.widthProperty());

        // Set CSS styles
        File cssFile = new File(App.class.getClassLoader().getResource("styles.css").getFile());
        scene.setUserAgentStylesheet(cssFile.toURI().toString());

        // Initializing the Application services.
        root.setCenter(controller.inputGrid);
        root.setBottom(controller.predictionGrid);
        controller.resetPrediction();

        window.setScene(scene);
        window.show();
    }

}