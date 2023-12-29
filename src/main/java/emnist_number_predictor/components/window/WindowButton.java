package emnist_number_predictor.components.window;

import emnist_number_predictor.app.App;
import emnist_number_predictor.util.HandleButton;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class WindowButton extends Button {

    private static final double WINDOW_BUTTON_WIDTH = 16;
    private static final double WINDOW_BUTTON_HEIGHT = 20;

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
        this.getStyleClass().add("window-button");
		this.setOnAction(enumValue.handleButton);
        this.setAlignment(Pos.CENTER);

        this.setPrefSize(WINDOW_BUTTON_WIDTH, WINDOW_BUTTON_HEIGHT);
        this.setMaxSize(WINDOW_BUTTON_WIDTH, WINDOW_BUTTON_HEIGHT);
    }

}