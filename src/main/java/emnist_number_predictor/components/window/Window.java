package emnist_number_predictor.components.window;
import static emnist_number_predictor.util.Const.*;

import lombok.extern.slf4j.Slf4j;
import java.net.URISyntaxException;
import java.net.URL;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@Slf4j
public class Window extends Stage {
    
    private static final String WINDOW_DEFAULT_STYLE = "window-default";
    private static final String APP_CSS_FILENAME = "app.css";
    private static final String LOADING_SCREEN_CSS_FILENAME = "loading-screen.css";

    public ObjectProperty<Double> width = new SimpleObjectProperty<Double>(this, "window-width", INIT_WINDOW_WIDTH);
    public ObjectProperty<Double> height = new SimpleObjectProperty<Double>(this, "window-height", INIT_WINDOW_HEIGHT);

    // Enum to store each control button EventHandler
    public enum STYLESHEET {
        APPLICATION(APP_CSS_FILENAME), 
        LOADING_SCREEN(LOADING_SCREEN_CSS_FILENAME);

        String filename;
        private STYLESHEET(String filename) {
            this.filename = filename;
        }

        public String getPath() {
            URL url = Window.class.getClassLoader().getResource(this.filename);
            try {
                return url.toURI().toString();
            } catch (URISyntaxException exception) {
                log.error("Error finding CSS file %s", this.filename);
                return null;
            }
        };
    }

    public Window(Pane root) {
        root.getStyleClass().add(WINDOW_DEFAULT_STYLE);

        this.setMinWidth(INIT_WINDOW_WIDTH);
        this.setMinHeight(INIT_WINDOW_HEIGHT);

        width.bind(root.widthProperty().asObject());
        height.bind(root.heightProperty().asObject());
    }

}
