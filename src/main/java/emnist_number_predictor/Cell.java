package emnist_number_predictor;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class Cell extends StackPane{

    private int row;
    private int column;
    private boolean isColored;
    private BufferedImage mnistImage;

    public Cell(int row, int column, BufferedImage mnistImage) {
        this.row = row;
        this.column = column;
        this.mnistImage = mnistImage;
        
        this.getStyleClass().add("cell");
        this.setId("cell");
        
        // Listener for the hover
     	this.hoverProperty().addListener(new ChangeListener<Boolean>(){
     		@Override
     		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
     			if(newValue){
     				setId("cell-hover-highlight");
     			}else{
     				setId("cell");               	
     			}
     		}
     	});

     	// Handler for Dragging while mouse held down
     	this.setOnDragDetected(new EventHandler<MouseEvent>(){
     		@Override
     		public void handle(MouseEvent event) {
     			Cell cell = (Cell) event.getSource();
     			cell.startFullDrag();
     		}
     	});
     	
     	// Handler for Dragging to color/erase each square
     	this.setOnMousePressed(new EventHandler<MouseEvent>(){
     		@Override
     		public void handle(MouseEvent event) {
     			changeColorHelper(event);
     		}
     	});
     	
     	// Handler for Dragging to color/erase each square
     	this.setOnMouseDragEntered(new EventHandler<MouseEvent>(){
     		@Override
     		public void handle(MouseEvent event) {
     			changeColorHelper(event);
     		}
     	});
     	
     }
    
    // Helper Method for changing the Cell StackPane color
    private void changeColorHelper(MouseEvent event){
    	Cell cell = (Cell) event.getSource();
    	if( event.isPrimaryButtonDown()) {				// Left Mouse Button color the square
    		cell.setStyle("-fx-background-color: -fx-cell-black;");
    		this.setColor(true);
    		
    		// Set a 2x2 for each cell colored
    		mnistImage.setRGB(row, column, Color.WHITE.getRGB());
    		mnistImage.setRGB(row + 1, column, Color.WHITE.getRGB());
    		mnistImage.setRGB(row, column + 1, Color.WHITE.getRGB());
    		mnistImage.setRGB(row + 1, column + 1, Color.WHITE.getRGB());
    	}else if( event.isSecondaryButtonDown()) {		// Right Mouse Button erase the square
    		cell.setStyle("-fx-background-color: -fx-cell-white;");
    		this.setColor(false);
    		
    		// Set a 2x2 for each cell erased
    		mnistImage.setRGB(row, column, Color.BLACK.getRGB());
    		mnistImage.setRGB(row + 1, column, Color.BLACK.getRGB());
    		mnistImage.setRGB(row, column + 1, Color.BLACK.getRGB());
    		mnistImage.setRGB(row + 1, column + 1, Color.BLACK.getRGB());
    	}
		GridHelper.runPrediction();
    }
    
    // Getters and Setters
    protected int getRow(){
    	return row;
    }
    protected int getColumn(){
    	return column;
    }
    protected void setColor(boolean color){
    	isColored = color;
    }
    
    
    public String toString() {
        return "(" + this.row + "," + this.column + ")";
    }
	public boolean isColored() {
		return isColored;
	}
}
