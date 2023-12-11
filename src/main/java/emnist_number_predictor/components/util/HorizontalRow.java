package emnist_number_predictor.components.util;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class HorizontalRow extends HBox{
    
    public HorizontalRow(double spacing, Pos alignment, Node... nodes) {
        this.setSpacing(spacing);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(nodes);
    }

}
