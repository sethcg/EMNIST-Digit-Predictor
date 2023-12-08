package emnist_number_predictor.components;

import emnist_number_predictor.service.AppService;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class InputCell extends StackPane {

	public int row, column, colorValue;
	private static final String INPUT_CELL_DEFAULT_STYLE = "input-cell-default";

	private ObjectProperty<Double> windowWidth = new SimpleObjectProperty<Double>(this, "windowWidth", null);
	private ObjectProperty<Double> windowHeight = new SimpleObjectProperty<Double>(this, "windowHeight", null);
	private ObjectProperty<Double> cellWidth = new SimpleObjectProperty<Double>(this, "cellWidth", null);
	private ObjectProperty<Double> cellHeight = new SimpleObjectProperty<Double>(this, "cellHeight", null);

    public InputCell(AppService app, int row, int column) {
		this.getStyleClass().add(INPUT_CELL_DEFAULT_STYLE);
		
		this.row = row;
		this.column = column;

		// Bind window size variables locally. 
		windowWidth.bind(app.windowWidth);
		windowHeight.bind(app.windowHeight);

		// Initialize cell size. 
		this.prefWidthProperty().bind(cellWidth);
		this.prefHeightProperty().bind(cellHeight);
		cellWidth.setValue(windowWidth.get() / InputGrid.GRID_SIZE);
		cellHeight.setValue(windowHeight.get() / InputGrid.GRID_SIZE);

		// Adjust cell size, when the Appliction window width or height changes.
		app.windowWidth.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				cellWidth.setValue(windowWidth.get() / InputGrid.GRID_SIZE);
			}
		});
		app.windowHeight.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				cellWidth.setValue(windowHeight.get() / InputGrid.GRID_SIZE);
			}
		});

     	// Select or Deselect cell on MousePress
     	this.setOnMousePressed(new EventHandler<MouseEvent>(){
     		@Override
     		public void handle(MouseEvent event) { updateColor(event); }
     	});
     	
     	// Select or Deselect cells on MouseDrag
        this.setOnDragDetected(new EventHandler<MouseEvent>(){
     		@Override
     		public void handle(MouseEvent event) { ((InputCell) event.getSource()).startFullDrag(); }
     	});
     	this.setOnMouseDragEntered(new EventHandler<MouseEvent>(){
     		@Override
     		public void handle(MouseEvent event) { updateColor(event); }
     	});
    }

    private void updateColor(MouseEvent event){
    	InputCell inputCell = (InputCell) event.getSource();
		if(event.isPrimaryButtonDown()) { 
			// Left MouseButton Selects
			inputCell.setStyle("-fx-background-color: -fx-cell-black;");
			inputCell.colorValue = 255;
    	} else if(event.isSecondaryButtonDown()) { 
			// Right MouseButton Erases
			inputCell.setStyle("-fx-background-color: -fx-cell-white;");
			inputCell.colorValue = 0;
    	}
		AppService.predictionService.updatePrediction(inputCell);
    }

}
