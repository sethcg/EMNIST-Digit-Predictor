package emnist_digit_predictor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class GridHelper{

	private final int gridMultiplier = 2;
	private final int gridInset = 4;
	
	private GridPane mainGrid = new GridPane();
	private BorderPane root;
	private int numRows;
	private int numColumns;
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	private static ArrayList<DigitVisual> digits = new ArrayList<DigitVisual>();
	
	protected static BufferedImage mnistImage = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_BINARY);

	private ObjectProperty<Double> windowWidth = new SimpleObjectProperty<Double>(this, "windowWidth", null);
	private ObjectProperty<Double> windowHeight = new SimpleObjectProperty<Double>(this, "windowHeight", null);
	private ObjectProperty<Double> cellWidth = new SimpleObjectProperty<Double>(this, "cellWidth", null);
	private ObjectProperty<Double> cellHeight = new SimpleObjectProperty<Double>(this, "cellHeight", null);
	
	public GridHelper(BorderPane root, int gridSize){
		this.root = root;
		this.numRows = gridSize;
		this.numColumns = gridSize;
		
		// Initialize the predictions, to avoid lag spike at beginning
		runPrediction();
		
		// Initialize mnistImage all black
		Graphics2D graphics = mnistImage.createGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect ( 0, 0, mnistImage.getWidth(), mnistImage.getHeight());
		
		// Add DigitVisuals
		for(int i = 0; i < 10; i++){
			digits.add(new DigitVisual(i));
		}
		    
		// Binding the Window Size to the local variables
		windowWidth.bind(root.widthProperty().asObject());
		windowHeight.bind(root.heightProperty().asObject());
		
		// Initialize the cell size
		cellWidth.setValue(windowWidth.get() / numRows);
		cellHeight.setValue(windowHeight.get() / numColumns);
		
		// Update Sizing as WindowSize Updates
		windowWidth.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				cellWidth.setValue(windowWidth.get() / numRows);
				
				// Maintain Square Grid
				mainGrid.setMaxWidth(windowHeight.get() * 0.6);
				mainGrid.setMaxHeight(windowWidth.get());
		    	mainGrid.setPadding(new Insets((windowHeight.get() * 0.05)));
			}
		});
		windowHeight.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				cellHeight.setValue(windowHeight.get() / numColumns);
				
				// Maintain Square Grid
				mainGrid.setMaxWidth(windowHeight.get() * 0.6);
				mainGrid.setMaxHeight(windowWidth.get());
		    	mainGrid.setPadding(new Insets((windowHeight.get() * 0.05)));
			}
		});
	}
	
	// Create Center of root BorderPane
	protected GridPane addCenter(){
		
		// Create Grid
		mainGrid.setPrefWidth(windowWidth.get());
		mainGrid.setPrefHeight(windowHeight.get());
		
		// Keep Grid Square
		mainGrid.setMaxWidth(windowHeight.get() * 0.6);
		mainGrid.setMaxHeight(windowWidth.get());
		
		// Creating each Cell 10x10, formated into 20x20 the Cell Class
    	for (int row = 0; row < numRows; row++) {
    		for (int col = 0; col < numColumns; col++) {
    			Cell cell = new Cell((row * gridMultiplier) + gridInset, (col * gridMultiplier) + gridInset, mnistImage);
    			cell.prefWidthProperty().bind(cellWidth);
    			cell.prefHeightProperty().bind(cellHeight);
    			mainGrid.add(cell, row, col);
    			cell.setColor(false);
    			cells.add(cell);
    		}
    	}
    	
    	mainGrid.setPadding(new Insets((windowHeight.get() * 0.05)));
    	
    	return mainGrid;
	}
	
	// Create Bottom of root BorderPane
	protected GridPane addBottom(){
		GridPane grid = new GridPane();
		grid.setPrefWidth(windowWidth.get());
		grid.setPrefHeight(windowHeight.get() * 0.4);	// 40% of screen space Vertically
		grid.setStyle("-fx-background-color: -fx-divider;");
		grid.setPadding(new Insets(0,10,0,10));
		grid.setAlignment(Pos.CENTER);
	    
	    RowConstraints top = new RowConstraints();
	    top.setMinHeight(50);
	    top.setVgrow(Priority.NEVER);
	    top.setValignment(VPos.CENTER);
	    RowConstraints bottom = new RowConstraints();
	    bottom.setVgrow(Priority.ALWAYS);
	    bottom.setValignment(VPos.CENTER);
	    grid.getRowConstraints().addAll(top, bottom);
		
		// Reset Button
		HBox hboxLeft = new HBox();
		hboxLeft.setAlignment(Pos.CENTER_LEFT);
		
		Button resetButton = new Button("Reset Image");
		resetButton.setId("Button");
		resetButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				root.setCenter(addCenter());
				for(int i = 0; i < digits.size(); i++){
					digits.get(i).setPercent(0.0);
				}
				
				// Reset to all black image
				Graphics2D graphics = mnistImage.createGraphics();
				graphics.setColor(Color.BLACK);
				graphics.fillRect ( 0, 0, mnistImage.getWidth(), mnistImage.getHeight());
			}
		});
		hboxLeft.getChildren().add(resetButton);

		// Save Button
		HBox hboxRight = new HBox();
		hboxRight.setAlignment(Pos.CENTER_RIGHT);
				
		Button saveButton = new Button("Save Image");
		saveButton.setId("Button");
		saveButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				saveImage(mnistImage);
			}
		});
		hboxRight.getChildren().add(saveButton);
		
		GridPane both = new GridPane();
		both.setAlignment(Pos.CENTER);
		
		ColumnConstraints left = new ColumnConstraints();
		left.setPercentWidth(50);
		ColumnConstraints right = new ColumnConstraints();
		right.setPercentWidth(50);
	    both.getColumnConstraints().addAll(left, right);
	    
		both.add(hboxRight, 1, 0);
		both.add(hboxLeft, 0, 0);
		
		// Combining Buttons and Bottom Data Visuals
		grid.add(both, 0, 0);
		for(int i = 0; i < 10; i++){
			grid.add(digits.get(i), 0, i + 1);
		}
    	return grid;
	}
	
	// Called by each Cell Object to update predictions RealTime
	protected static void runPrediction(){
		try {
			File file;
			file = File.createTempFile("tempImage", ".png");
			ImageIO.write(mnistImage, "png", file);
			ArrayList<Double> predictions = PredictionHelper.getPredictions(file);
			for(int i = 0; i < digits.size(); i++){
				digits.get(i).setPercent(predictions.get(i));
			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Problem running predictions");
		}
	}
	
	// Save the Grid as a PNG file
	private static void saveImage(BufferedImage image){
		String path = "./tempImage.png";
		File ImageFile = new File(path);
		try{
			ImageIO.write(image, "png", ImageFile);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
