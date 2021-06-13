package deeplearning_test;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class Cell extends StackPane{

    int row;
    int column;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        
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
    		cell.setStyle("-fx-background-color: #4c4c4c;");
    	}else if( event.isSecondaryButtonDown()) {		// Right Mouse Button erase the square
    		cell.setStyle("-fx-background-color: #efefef;");
    	}
    }
    
    // Getters and Setters
    protected int getRow(){
    	return row;
    }
    protected int getColumn(){
    	return column;
    }
    
    public String toString() {
        return "(" + this.row + "," + this.column + ")";
    }
}
