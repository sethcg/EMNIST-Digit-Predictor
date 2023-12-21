package emnist_number_predictor.components.controls;

import javafx.geometry.Pos;
import emnist_number_predictor.app.AppController;
import emnist_number_predictor.util.HandleButton;
import javafx.scene.control.Button;

public class ControlButton extends Button {

    // Enum ButtonHandle
    public static enum FUNCTION {
        SAVE(() -> { AppController.saveScreenshot(); }),
        RESET(() -> { AppController.resetPrediction(); });

        public HandleButton<Void> handleButton;

        private FUNCTION(Runnable method) {
            this.handleButton = new HandleButton<Void>(method);
        };
    }

    public ControlButton(String text, Pos position, FUNCTION enumValue) {
        this.getStyleClass().add("control-button");
        this.setPrefWidth(USE_COMPUTED_SIZE);
        this.setText(text);
		this.setOnAction(enumValue.handleButton);
        this.setAlignment(position);
    }

}
