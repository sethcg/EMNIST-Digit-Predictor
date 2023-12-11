package emnist_number_predictor.components.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TextLabel extends Label {

    private static final String LABEL_DEFAULT_STYLE = "label-default";

    public TextLabel(String text, Pos position, int width) {
        this.getStyleClass().add(LABEL_DEFAULT_STYLE);

        this.setText(text);
        this.setMinWidth(width);
        this.setAlignment(position);
        HBox.setHgrow(this, Priority.NEVER);
    }

}
