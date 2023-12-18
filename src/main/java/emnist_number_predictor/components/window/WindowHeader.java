package emnist_number_predictor.components.window;

import emnist_number_predictor.components.window.WindowButton.FUNCTION;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class WindowHeader extends GridPane {
    
    public static final double WINDOW_HEADER_HEIGHT = 32;
    private static final String WINDOW_HEADER_DEFAULT_STYLE = "window-header-default";

    public WindowHeader() {
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add(WINDOW_HEADER_DEFAULT_STYLE);

        this.setPrefHeight(WINDOW_HEADER_HEIGHT);
        this.setMaxHeight(WINDOW_HEADER_HEIGHT);
        this.prefWidthProperty().bind(Window.width);

        this.addColumnConstraints();

        // this.addColumn(0, new WindowButton("settings-window-button", WINDOW_HEADER_HEIGHT, FUNCTION.SETTINGS);

        HBox container = new HBox(16,
            new WindowButton("minimize-window-button", WINDOW_HEADER_HEIGHT, FUNCTION.MINIMIZE),
            new WindowButton("maximize-window-button", WINDOW_HEADER_HEIGHT, FUNCTION.MAXIMIZE),
            new WindowButton("close-window-button", WINDOW_HEADER_HEIGHT, FUNCTION.CLOSE));
        container.setAlignment(Pos.CENTER_RIGHT);
        container.setPadding(new Insets(0, 8, 0, 0));

        this.addColumn(1, container);
    }

    private void addColumnConstraints() {
        ColumnConstraints settingsButtons = new ColumnConstraints();
        settingsButtons.setHgrow(Priority.ALWAYS);
        settingsButtons.setHalignment(HPos.LEFT);

        ColumnConstraints windowButtons = new ColumnConstraints();
        windowButtons.setHgrow(Priority.ALWAYS);
        windowButtons.setHalignment(HPos.RIGHT);
        windowButtons.setPrefWidth(96);
        windowButtons.setMinWidth(96);
        windowButtons.setHgrow(Priority.ALWAYS);

        this.getColumnConstraints().addAll(settingsButtons, windowButtons);
    }

}
