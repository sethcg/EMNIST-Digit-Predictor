package emnist_number_predictor.components;

import emnist_number_predictor.service.AppService;
import emnist_number_predictor.service.PredictionService;
import emnist_number_predictor.helpers.ControlButton;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class ControlBar extends GridPane {

	public ControlBar() {
		this.setAlignment(Pos.CENTER);

		ColumnConstraints left = new ColumnConstraints();
		left.setPercentWidth(50);
		ColumnConstraints right = new ColumnConstraints();
		right.setPercentWidth(50);

	    this.getColumnConstraints().addAll(left, right);
		this.addRow(0, this.makeResetButton(), this.makeSaveButton());
	}

	public ControlButton makeResetButton() {
		ControlButton resetButton = new ControlButton("Reset", Pos.CENTER_LEFT, new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				PredictionService predictionService = AppService.getPredictionService();
				predictionService.resetPrediction();
			}
		});
		return resetButton;
    }

	public ControlButton makeSaveButton() {
		ControlButton saveButton = new ControlButton("Save Image", Pos.CENTER_RIGHT, new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) { 
				AppService.getGridService().saveImage(); 
			}
		});
        return saveButton;
    }

}
