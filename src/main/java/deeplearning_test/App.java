package deeplearning_test;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

	// Initial Variables
    private int gridSize = 10;			// 10 x 10 GridSize
    private int initialWidth = 400;
    private int initialHeight = 600;

    
    @Override
    public void init() throws IOException{
    	// Load the Model (Takes a couple seconds)
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
        scene.setUserAgentStylesheet(new File("./styles.css").toURI().toString());
        stage.setScene(scene);
        stage.setTitle("Digit Predictor");
        stage.show();

    }

    
    // Code Needed to Launch in Eclipse IDE
    public static void main(String[] args) {
        launch(args);
    }
}
