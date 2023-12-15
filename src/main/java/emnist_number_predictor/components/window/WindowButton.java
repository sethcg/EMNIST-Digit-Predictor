package emnist_number_predictor.components.window;

import javafx.geometry.Pos;
import emnist_number_predictor.app.App;
import emnist_number_predictor.util.HandleButton;
import javafx.scene.control.Button;

public class WindowButton extends Button {

    private static final String WINDOW_BUTTON_DEFAULT_STYLE = "window-button-default";

    // Enum ButtonHandle
    public static enum FUNCTION {
        MINIMIZE(() -> { App.minimize(); }),
        MAXIMIZE(() -> { App.maximize(); }),
        CLOSE(() -> { App.close(); });

        public HandleButton<Void> handleButton;

        private FUNCTION(Runnable method) {
            this.handleButton = new HandleButton<Void>(method);
        };
    }

    public WindowButton(String id, double size, FUNCTION enumValue) {
        this.setId(id);
        this.getStyleClass().add(WINDOW_BUTTON_DEFAULT_STYLE);
		this.setOnAction(enumValue.handleButton);
        this.setAlignment(Pos.CENTER);

        this.setPrefSize(16, 20);
        this.setMaxSize(16, 20);
    }

}