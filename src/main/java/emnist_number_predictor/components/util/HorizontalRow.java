package emnist_number_predictor.components.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class HorizontalRow extends HBox{
    
    public HorizontalRow(double spacing, Pos alignment, Node... nodes) {
        this.setSpacing(spacing);
        this.setAlignment(alignment);
        this.getChildren().addAll(nodes);
    }

    public HorizontalRow(double spacing, Pos alignment, Insets padding, Node... nodes) {
        this(spacing, alignment, nodes);
        this.setPadding(padding);
    }

}
