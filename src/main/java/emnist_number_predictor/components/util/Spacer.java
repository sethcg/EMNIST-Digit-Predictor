package emnist_number_predictor.components.util;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public final class Spacer extends Pane {
    
    public Spacer() {
        HBox.setHgrow(this, Priority.ALWAYS);
    }
}
