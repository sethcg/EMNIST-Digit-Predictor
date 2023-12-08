package emnist_number_predictor.helpers;

import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ControlButton extends HBox {

    private static final String CONTROL_BUTTON_DEFAULT_STYLE = "control-button-default";

    public ControlButton(String text, Pos position, EventHandler<ActionEvent> eventHandler) {
        // Button
        Button button = new Button(text);
        button.getStyleClass().add(CONTROL_BUTTON_DEFAULT_STYLE);
		button.setOnAction(eventHandler);

        // Button container
        this.setAlignment(position);
        this.getChildren().add(button);
    }
}
