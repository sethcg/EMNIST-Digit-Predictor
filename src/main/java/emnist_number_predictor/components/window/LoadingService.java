package emnist_number_predictor.components.window;
import static emnist_number_predictor.util.Const.*;

import lombok.extern.slf4j.Slf4j;
import java.io.File;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window.STYLESHEET;
import emnist_number_predictor.model.ConfigurationProgress;
import emnist_number_predictor.model.Model;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.util.Duration;

@Slf4j
public class LoadingService {

    private static final String INIT_PROGRESS_TEXT = "";
    private static final double INIT_PROGRESS_PERCENT = 0.0;

    public static final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
    public static final ReadOnlyStringWrapper progressText = new ReadOnlyStringWrapper("configuration-progress-text");

    private static LoadingScreen loadingScreen = new LoadingScreen();

    public LoadingService() {
        progress.set(INIT_PROGRESS_PERCENT);
        progressText.set(INIT_PROGRESS_TEXT);

        loadingScreen.progressBar.progressProperty().bind(LoadingService.progress);
        loadingScreen.configurationText.textProperty().bind(LoadingService.progressText);
    }

    private static Task<Void> initializeModel() {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                boolean hasModel = false;
                File file = new File(MODEL_PATH);
                try {
                    hasModel = file.createNewFile() || ENABLE_DEBUG_REBUILD_MODEL_OPTION;
                } catch (Exception exception) {
                    log.error("Error creating the %s file.", MODEL_FILE_NAME);
                } finally {
                    Model model = new Model(file, new ConfigurationProgress(hasModel));
                    App.controller.initializeModel(model);
                }
                return null;
            }
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progress.set(INIT_PROGRESS_PERCENT);
                progressText.set(INIT_PROGRESS_TEXT);
            }
        });

        // Add FadeTransition between Loading and App scene
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                FadeTransition loadingTransition = new FadeTransition(Duration.seconds(0.5), loadingScreen);
                loadingTransition.setFromValue(1.0);
                loadingTransition.setToValue(0.0);
                loadingTransition.setOnFinished(actionEvent -> App.showApplication(App.window));
                loadingTransition.play();
            }
        });

        return task;
    }

    public static void setProgress(double percentComplete, String configurationText) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progress.set(percentComplete);
                progressText.set(configurationText);
            }
        });
    }

    public static void showLoadingScreen(Window window) {
        new Thread(initializeModel()).start();

        Scene loadingScene = new Scene(loadingScreen, INIT_WINDOW_WIDTH, INIT_WINDOW_HEIGHT, Color.TRANSPARENT);
        loadingScene.setUserAgentStylesheet(STYLESHEET.LOADING_SCREEN.getPath());

        loadingScreen.prefHeightProperty().bind(loadingScene.heightProperty());
    	loadingScreen.prefWidthProperty().bind(loadingScene.widthProperty());

        window.setScene(loadingScene);
        window.show();
    }

}