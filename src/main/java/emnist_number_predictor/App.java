package emnist_number_predictor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

	// Initial Variables
    private int gridSize = 10;  // 10 x 10 GridSize
    private int initialWidth = 400;
    private int initialHeight = 600;
    
    @Override
    public void init() throws IOException, URISyntaxException{
    	// Load the Model (Takes a couple seconds).
    	PredictionHelper.loadModel();
    }
    
    @Override
    public void start(Stage stage) {
    	
    	BorderPane root = new BorderPane();
    	root.setStyle("-fx-background-color: -fx-grid-outline;");
    	
    	// Create Scene and Stage
        Scene scene = new Scene(root, initialWidth, initialHeight);
    	root.prefHeightProperty().bind(scene.heightProperty());
    	root.prefWidthProperty().bind(scene.widthProperty());
    	
    	// Add Grid to Screen
    	GridHelper grid = new GridHelper(root, gridSize);
    	root.setCenter(grid.addCenter());
    	root.setBottom(grid.addBottom());

    	// Finish Setup of JavaFX GUI
		File cssFile = new File(App.class.getClassLoader().getResource("styles.css").getFile());
        scene.setUserAgentStylesheet(cssFile.toURI().toString());
        stage.setScene(scene);
        stage.setTitle("Digit Predictor");
        stage.show();

    }
 
    // Code Needed to Launch
    public static void main(String[] args) {
        launch(args);
    }
}
