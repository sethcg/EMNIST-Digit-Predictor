package emnist_number_predictor.components.window;
import static emnist_number_predictor.util.Const.*;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Window extends Stage {
    
    private static final String WINDOW_DEFAULT_STYLE = "window-default";
    
    public ObjectProperty<Double> width = new SimpleObjectProperty<Double>(this, "window-width", INIT_WINDOW_WIDTH);
    public ObjectProperty<Double> height = new SimpleObjectProperty<Double>(this, "window-height", INIT_WINDOW_HEIGHT);

    public Window(Pane root) {
        root.getStyleClass().add(WINDOW_DEFAULT_STYLE);
        
        this.setMinWidth(INIT_WINDOW_WIDTH);
        this.setMinHeight(INIT_WINDOW_HEIGHT);

        width.bind(root.widthProperty().asObject());
        height.bind(root.heightProperty().asObject());
    }

}
