package emnist_number_predictor.service;
import static emnist_number_predictor.util.Const.*;

import lombok.extern.slf4j.Slf4j;
import java.io.File;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window;
import emnist_number_predictor.components.window.Window.STYLESHEET;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.util.Duration;

@Slf4j
public class LoadingService {

    private static boolean hasModel = createModelFile();
    private static LoadingScreen loadingScreen = new LoadingScreen(hasModel);

    public static final ReadOnlyDoubleWrapper configurationProgress = new ReadOnlyDoubleWrapper();
    public static final ReadOnlyStringWrapper configurationProgressText = new ReadOnlyStringWrapper("configuration-progress-text");

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

    private static Task<Void> initializeModel() {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                ModelService.initializeModel(hasModel);
                return null;
            }
        };

        // Add FadeTransition between Loading and App scene
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // loadingScreen.ellipsis.setVisible(false);
                loadingScreen.transition.stop();

                FadeTransition loadingTransition = new FadeTransition(Duration.seconds(0.5), loadingScreen);
                loadingTransition.setFromValue(1.0);
                loadingTransition.setToValue(0.0);
                loadingTransition.setOnFinished(actionEvent -> App.showApplication());
                loadingTransition.play();
            }
        });

        return task;
    }

    public static void setConfigurationText(String configurationText) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                configurationProgressText.set(configurationText);
            }
        });
    }

    public static void setConfigurationProgress(double percentComplete) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                configurationProgress.set(percentComplete);
            }
        });
    }

    public static void showConfigurationProgress(double percentComplete) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loadingScreen.configurationProgressBar.setVisible(true);
            }
        });
    }

    public static void hideConfigurationProgress(double percentComplete) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loadingScreen.configurationProgressBar.setVisible(false);
            }
        });
    }

    public static void showLoadingScreen(Window window) {
        new Thread(initializeModel()).start();

        loadingScreen.configurationProgressBar.progressProperty().bind(LoadingService.configurationProgress);
        loadingScreen.configurationText.textProperty().bind(LoadingService.configurationProgressText);

        Scene loadingScene = new Scene(loadingScreen, INIT_WINDOW_WIDTH, INIT_WINDOW_HEIGHT, Color.TRANSPARENT);
        loadingScene.setUserAgentStylesheet(STYLESHEET.LOADING_SCREEN.getPath());

        loadingScreen.prefHeightProperty().bind(loadingScene.heightProperty());
    	loadingScreen.prefWidthProperty().bind(loadingScene.widthProperty());

        window.setScene(loadingScene);
        window.show();
    }

}