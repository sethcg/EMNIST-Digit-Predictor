package emnist_number_predictor.components.window;

import emnist_number_predictor.components.util.HorizontalRow;
import emnist_number_predictor.components.util.TextLabel;
import emnist_number_predictor.components.window.WindowButton.FUNCTION;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class WindowHeader extends GridPane {
    
    public static final double WINDOW_HEADER_HEIGHT = 32;

    public WindowHeader() {
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("window-header");

        this.setPrefHeight(WINDOW_HEADER_HEIGHT);
        this.setMaxHeight(WINDOW_HEADER_HEIGHT);
        this.prefWidthProperty().bind(Window.width);

        this.addColumnConstraints();

        // Window Title
        this.addColumn(0, new HorizontalRow(16, 
            Pos.CENTER_LEFT, new Insets(0, 0, 0, 8),
            new TextLabel("EMNIST Digit Predictor", Pos.CENTER_LEFT, 10, "window-header-text")));

        // Window Buttons
        this.addColumn(1, new HorizontalRow(16, 
            Pos.CENTER_RIGHT, new Insets(0, 8, 0, 0),
            new WindowButton("minimize-window-button", WINDOW_HEADER_HEIGHT, FUNCTION.MINIMIZE),
            new WindowButton("maximize-window-button", WINDOW_HEADER_HEIGHT, FUNCTION.MAXIMIZE),
            new WindowButton("close-window-button", WINDOW_HEADER_HEIGHT, FUNCTION.CLOSE)));
    }

    private void addColumnConstraints() {
        ColumnConstraints windowTitle = new ColumnConstraints();
        windowTitle.setHgrow(Priority.ALWAYS);
        windowTitle.setHalignment(HPos.LEFT);

        ColumnConstraints windowButtons = new ColumnConstraints();
        windowButtons.setHgrow(Priority.ALWAYS);
        windowButtons.setHalignment(HPos.RIGHT);
        windowButtons.setPrefWidth(96);
        windowButtons.setMinWidth(96);
        windowButtons.setHgrow(Priority.ALWAYS);

        this.getColumnConstraints().addAll(windowTitle, windowButtons);
    }

}
