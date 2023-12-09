package emnist_number_predictor.components.prediction;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PredictionLabel extends Label {

    private static final String LABEL_DEFAULT_STYLE = "label-default";

    public PredictionLabel(String text, int width) {
        this.getStyleClass().add(LABEL_DEFAULT_STYLE);

        this.setText(text);
        this.setMinWidth(width);
        this.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(this, Priority.NEVER);
    }

}
