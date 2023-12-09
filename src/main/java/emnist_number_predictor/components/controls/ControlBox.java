package emnist_number_predictor.components.controls;

import emnist_number_predictor.components.controls.ControlButton.FUNCTION;
import emnist_number_predictor.components.util.Spacer;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class ControlBox extends HBox {

	public ControlBox() {

		this.getChildren().addAll(
			new ControlButton("Reset", Pos.CENTER_LEFT, FUNCTION.RESET), 
			new Spacer(),
			new ControlButton("Save Image", Pos.CENTER_RIGHT, FUNCTION.SAVE));
	}

}
