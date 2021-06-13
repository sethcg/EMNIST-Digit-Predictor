package deeplearning_test;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

	// Initial Variables
    int numRows = 28;
    int numColumns = 28;
    int initialWidth = 800;
    int initialHeight = 600;

    @Override
    public void start(Stage stage) {
    	
    	BorderPane root = new BorderPane();
    	
    	// Create Scene and Stage
        Scene scene = new Scene(root, initialWidth, initialHeight);
    	root.prefHeightProperty().bind(scene.heightProperty());
    	root.prefWidthProperty().bind(scene.widthProperty());
    	
    	// Add Grid to Screen
    	GridHelper grid = new GridHelper(root, numRows, numColumns);
    	root.setCenter(grid.addCenter());
    	root.setBottom(grid.addBottom());

    	// Finish Setup of JavaFX GUI
        scene.setUserAgentStylesheet(new File("./styles.css").toURI().toString());
        stage.setScene(scene);
        stage.setTitle("MP3 Audio Player");
        stage.show();

    }

    
    // Code Needed to Launch in Eclipse IDE
    public static void main(String[] args) {
        launch(args);
    }
}
