package deeplearning_test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class GridHelper {
	
	BorderPane root;
	int numRows;
	int numColumns;
	
	ObjectProperty<Double> windowWidth = new SimpleObjectProperty<Double>(this, "windowWidth", null);
	ObjectProperty<Double> windowHeight = new SimpleObjectProperty<Double>(this, "windowHeight", null);
	ObjectProperty<Double> cellWidth = new SimpleObjectProperty<Double>(this, "cellWidth", null);
	ObjectProperty<Double> cellHeight = new SimpleObjectProperty<Double>(this, "cellHeight", null);
	
	public GridHelper(BorderPane root, int numRows, int numColumns){
		this.root = root;
		this.numRows = numRows;
		this.numColumns = numColumns;
		
		// Binding the Window Size to the local variables
		windowWidth.bind(root.widthProperty().asObject());
		windowHeight.bind(root.heightProperty().asObject());
		
		// Initialize the cell size
		cellWidth.setValue(windowWidth.get() / numRows);
		cellHeight.setValue(windowHeight.get() / numColumns); 
		
		// Listener that sets the Cell Size responsively
		windowWidth.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				cellWidth.setValue(windowWidth.get() / numRows);
			}
		});
		windowHeight.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				cellHeight.setValue(windowHeight.get() / numColumns);
			}
		});
	}
	
	
	protected GridPane addCenter(){	
		// Create Grid
		GridPane grid = new GridPane();
		grid.setPrefWidth(windowWidth.get());
		grid.setPrefHeight(windowHeight.get());
		
		// Creating each Cell
    	for (int row = 0; row < numRows; row++) {
    		for (int col = 0; col < numColumns; col++) {
    			Cell cell = new Cell(row, col);
    			cell.prefWidthProperty().bind(cellWidth);
    			cell.prefHeightProperty().bind(cellHeight);
    			grid.add(cell, row, col);
    		}
    	}
    	return grid;
	}
	
	protected GridPane addBottom(){
		// Create Bottom Panel
		GridPane grid = new GridPane();
		grid.setPrefWidth(windowWidth.get());
		grid.setPrefHeight(48.0);
		grid.setStyle("-fx-background-color: #141414;");
		grid.setPadding(new Insets(0,10,0,10));
		grid.setAlignment(Pos.CENTER);
		
		ColumnConstraints left = new ColumnConstraints();
	    left.setPercentWidth(50);
	    ColumnConstraints right = new ColumnConstraints();
	    right.setPercentWidth(50);
	    grid.getColumnConstraints().addAll(left, right);
		
		// Left Side, Reset Button
		HBox hboxLeft = new HBox();
		hboxLeft.setAlignment(Pos.CENTER_LEFT);
		
		Button resetButton = new Button("Reset Image");
		resetButton.setId("Button");
		resetButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				root.setCenter(addCenter());
			}
		});
		hboxLeft.getChildren().add(resetButton);
		
		// Right Side, Predict Button
		HBox hboxRight = new HBox();
		hboxRight.setAlignment(Pos.CENTER_RIGHT);
		
		Button predictButton = new Button("Predict Image");
		predictButton.setId("Button");
		predictButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				System.out.println("PREDICT BUTTON PRESSED");
			}
		});
		hboxRight.getChildren().add(predictButton);

		// Adding to the grid
		grid.add(hboxLeft, 0, 0);
		grid.add(hboxRight, 1, 0);

    	return grid;
	}
	
}
