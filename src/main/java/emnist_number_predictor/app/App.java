package emnist_number_predictor.app;

import emnist_number_predictor.service.AppService;
import org.nd4j.common.config.ND4JSystemProperties;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	BorderPane root = new BorderPane();

        // Set the Width and Height.
        Scene scene = new Scene(root, AppService.INIT_WIDTH, AppService.INIT_HEIGHT);
    	root.prefHeightProperty().bind(scene.heightProperty());
    	root.prefWidthProperty().bind(scene.widthProperty());

        // Add the application elements: input grid, and prediction evaluations
        AppService app = new AppService(root);
        root.setCenter(AppService.getInputGrid());
        root.setBottom(AppService.getPredictionGrid());
        
        // Finish Setup of JavaFX GUI
		File cssFile = new File(App.class.getClassLoader().getResource("styles.css").getFile());
        scene.setUserAgentStylesheet(cssFile.toURI().toString());
        stage.setScene(scene);
        stage.setTitle("EMNIST Number Predictor");
        stage.show();
    }

    // Launch Application
    public static void main(String[] args) {
        // Disable default logging for ND4J.
        System.setProperty(ND4JSystemProperties.LOG_INITIALIZATION, "false");

        launch(args); 
    }
}