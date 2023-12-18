package emnist_number_predictor.components.window;
import static emnist_number_predictor.util.Const.*;

import lombok.extern.slf4j.Slf4j;
import java.net.URISyntaxException;
import java.net.URL;
import emnist_number_predictor.app.App;
import emnist_number_predictor.service.LoadingScreen;
import emnist_number_predictor.service.LoadingService;
import emnist_number_predictor.util.Draggable;
import emnist_number_predictor.util.Resizeable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

@Slf4j
public class Window extends Stage {

    private static final String APP_CSS_FILENAME = "app.css";
    private static final String LOADING_SCREEN_CSS_FILENAME = "loading-screen.css";

    private static BorderPane root = new BorderPane();
    private static Scene scene = new Scene(root, INIT_WINDOW_WIDTH, INIT_WINDOW_HEIGHT, Color.TRANSPARENT);
    public static LoadingScreen loadingScreen = new LoadingScreen(App.hasModel);

    public static DoubleProperty width = new SimpleDoubleProperty(INIT_WINDOW_WIDTH, "window-width");
    public static DoubleProperty height = new SimpleDoubleProperty(INIT_WINDOW_WIDTH, "window-width");

    // Enum to store CSS files.
    public static enum STYLESHEET {
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

    public void initialize() {
        this.setScene(scene);
        this.setMinWidth(INIT_WINDOW_WIDTH);
        this.setMinHeight(INIT_WINDOW_HEIGHT);

        // this.initStyle(StageStyle.UNDECORATED);
        this.setTitle("EMNIST Number Predictor");

        width.bind(root.widthProperty().asObject());
        height.bind(root.heightProperty().asObject());
    }

    public void setScene(STYLESHEET STYLESHEET) {
        scene.setUserAgentStylesheet(STYLESHEET.getPath());

        switch (STYLESHEET) {
            case APPLICATION -> {
                Draggable.addDraggableListener(App.window, root);
                Resizeable.addResizeableListener(App.window, root);

                root.setTop(new WindowHeader());
                root.setCenter(App.controller.inputGrid);
                root.setBottom(App.controller.predictionGrid);
            }
            case LOADING_SCREEN -> {
                new Thread(LoadingService.initializeModel()).start();

                // Bind configuration progress values
                loadingScreen.configurationProgressBar.progressProperty().bind(LoadingService.configurationProgress);
                loadingScreen.configurationText.textProperty().bind(LoadingService.configurationProgressText);

                Draggable.addDraggableListener(App.window, loadingScreen);
                root.setCenter(loadingScreen);
            }
        }
    }
}
