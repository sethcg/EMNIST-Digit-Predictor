package emnist_number_predictor.components.window;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class WindowFooter extends GridPane {
    
    public static final double WINDOW_FOOTER_SIZE = 15;
    private static final String RESIZE_FOOTER_DEFAULT_STYLE = "resize-footer-default";

    public WindowFooter() {
        this.setAlignment(Pos.CENTER_RIGHT);

        this.setPrefHeight(WINDOW_FOOTER_SIZE);
        this.setMaxHeight(WINDOW_FOOTER_SIZE);
        this.prefWidthProperty().bind(Window.width);

        Pane resizeFooter = new Pane();
        this.setPrefSize(WINDOW_FOOTER_SIZE, WINDOW_FOOTER_SIZE);
        this.setMaxSize(WINDOW_FOOTER_SIZE, WINDOW_FOOTER_SIZE);
        resizeFooter.getStyleClass().add(RESIZE_FOOTER_DEFAULT_STYLE);

        HBox container = new HBox(resizeFooter);
        container.setAlignment(Pos.CENTER_RIGHT);

        this.addColumn(1, container);
    }

}
