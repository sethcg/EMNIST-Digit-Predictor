package emnist_number_predictor.components.util;

import javafx.geometry.VPos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class RowConstraint extends RowConstraints {
    
    public RowConstraint(VPos position, Priority priority) {
        this.setValignment(position);
        this.setVgrow(priority);
    }

    public RowConstraint(VPos position, Priority priority, double minHeight) {
        this.setValignment(position);
        this.setVgrow(priority);
        this.setMinHeight(minHeight);
    }
}
