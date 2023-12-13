package emnist_number_predictor.service;

import emnist_number_predictor.app.App;
import emnist_number_predictor.components.window.Window;
import emnist_number_predictor.components.window.Window.STYLESHEET;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.util.Duration;

public class LoadingService {

    private static Window window = App.window;
    public static final ReadOnlyDoubleWrapper configurationProgress = new ReadOnlyDoubleWrapper();
    public static final ReadOnlyStringWrapper configurationProgressText = new ReadOnlyStringWrapper("configuration-progress-text");

    public static Task<Void> initializeModel() {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                ModelService.initializeModel(App.hasModel);
                return null;
            }
        };

        // Add FadeTransition between Loading and App scene
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                Window.loadingScreen.transition.stop();

                FadeTransition loadingTransition = new FadeTransition(Duration.seconds(0.5), Window.loadingScreen);
                loadingTransition.setFromValue(1.0);
                loadingTransition.setToValue(0.0);
                loadingTransition.setOnFinished(actionEvent -> window.setScene(STYLESHEET.APPLICATION));
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
                Window.loadingScreen.configurationProgressBar.setVisible(true);
            }
        });
    }

    public static void hideConfigurationProgress(double percentComplete) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Window.loadingScreen.configurationProgressBar.setVisible(false);
            }
        });
    }

}