package emnist_number_predictor.components.window;
import static emnist_number_predictor.util.Const.*;

import emnist_number_predictor.components.util.HorizontalRow;
import emnist_number_predictor.components.util.Spacer;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LoadingScreen extends VBox{

    private Label ellipsis = new Label("");
    public Label configurationText = new Label("");
    public ProgressBar progressBar = new ProgressBar(0.0);

    public LoadingScreen() {
        this.progressBar.setMaxWidth(Double.MAX_VALUE);
        VBox container = new VBox(10,
            getProgressContainer(progressBar),
            new HorizontalRow(10, Pos.CENTER, configurationText, ellipsis)
        );

        // Initialize ellipsis transition animation
        loadingTextAnimation(ellipsis, progressBar);

        this.getChildren().addAll(new Spacer(), container, new Spacer());
    }

    private static GridPane getProgressContainer(ProgressBar progressBar) {
        progressBar.setMaxWidth(Double.MAX_VALUE);

        GridPane container = new GridPane();
        container.setAlignment(Pos.CENTER);
        container.getColumnConstraints().addAll(new ColumnConstraints(INIT_WINDOW_WIDTH * 0.8));
        container.addRow(0, progressBar);
        return container;
    }

    private static void loadingTextAnimation(Label textLabel, ProgressBar progressBar) {
        SequentialTransition transition = new SequentialTransition (
            new PauseTransition(Duration.millis(1000)),
            new Transition() {
                @Override
                protected void interpolate(double frac) {
                    String text = textLabel.getText().equals("...") ? "" : textLabel.getText() + ".";
                    textLabel.setText(text);
                }
            
            }
        );
        transition.setCycleCount(Timeline.INDEFINITE);
        progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
            if(progressBar.getProgress() * 100 >= 80) {
                transition.stop();
            }
        });
        transition.playFromStart();
    }

}