package emnist_number_predictor.service;
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

    public Label configurationText = new Label("");
    public ProgressBar configurationProgressBar = new ProgressBar(0.0);

    public Label ellipsis = new Label("");
    public SequentialTransition transition  = new SequentialTransition (
            new PauseTransition(Duration.millis(1000)),
            new Transition() {
                @Override
                protected void interpolate(double frac) {
                    String text = ellipsis.getText().equals("...") ? "" : ellipsis.getText() + ".";
                    ellipsis.setText(text);
                }
            
            }
        );

    public LoadingScreen(boolean hasModel) {
        this.getChildren().addAll(new Spacer(), getContainer(hasModel), new Spacer());
    }

    private VBox getContainer(boolean hasModel) {
        if(hasModel) {
            return new VBox(10, new HorizontalRow(10, Pos.CENTER, configurationText));
        } else {
            configurationProgressBar.setMaxWidth(Double.MAX_VALUE);

            GridPane progressContainer = new GridPane();
            progressContainer.setAlignment(Pos.CENTER);
            progressContainer.getColumnConstraints().addAll(new ColumnConstraints(INIT_WINDOW_WIDTH * 0.8));
            progressContainer.addRow(0, configurationProgressBar);

            // Animate Ellipsis
            transition.setCycleCount(Timeline.INDEFINITE);
            transition.play();

            return new VBox(10, progressContainer, new HorizontalRow(10, Pos.CENTER, configurationText, ellipsis));
        }
    }

}