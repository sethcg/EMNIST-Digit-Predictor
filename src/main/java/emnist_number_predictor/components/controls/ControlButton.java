package emnist_number_predictor.components.controls;

import javafx.geometry.Pos;
import emnist_number_predictor.app.App;
import emnist_number_predictor.util.HandleButton;
import javafx.scene.control.Button;

public class ControlButton extends Button {

    private static final String CONTROL_BUTTON_DEFAULT_STYLE = "control-button-default";

    public ControlButton(String text, Pos position, FUNCTION enumValue) {
        this.setPrefWidth(USE_COMPUTED_SIZE);
        this.setText(text);
        this.getStyleClass().add(CONTROL_BUTTON_DEFAULT_STYLE);
		this.setOnAction(enumValue.handleButton);
        this.setAlignment(position);
    }

    // Enum ButtonHandle
    public static enum FUNCTION {
        SAVE(() -> { App.controller.saveScreenshot(); }),
        RESET(() -> { App.controller.resetPrediction(); });

        public HandleButton<Void> handleButton;

        private FUNCTION(Runnable method) {
            this.handleButton = new HandleButton<Void>(method);
        };
    }

}
