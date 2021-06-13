package deeplearning_test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

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
	
	
	protected GridPane gridCenter(){	
		//Create Grid
		GridPane grid = new GridPane();
		grid.setPrefWidth(windowWidth.get());
		grid.setPrefHeight(windowHeight.get());
		
		// Creating each Cell
    	for (int row = 0; row < numRows; row++) {
    		for (int col = 0; col < numColumns; col++) {
    			Cell cell = new Cell(row, col);
    			cell.prefWidthProperty().bind(cellWidth);
    			cell.prefHeightProperty().bind(cellHeight);
    			//makePaintable(cell);
    			grid.add(cell, row, col);
    		}
    	}
    	return grid;
	}
	
}
