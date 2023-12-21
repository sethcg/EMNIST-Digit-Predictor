package emnist_number_predictor.components.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TextLabel extends Label {

    public TextLabel(String text, Pos position, int width) {
        this.setText(text);
        this.setMinWidth(width);
        this.setAlignment(position);
        HBox.setHgrow(this, Priority.NEVER);
    }

    public TextLabel(String text, Pos position, int width, String id) {
        this(text, position, width);
        this.setId(id);
    }


}
