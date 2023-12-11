package emnist_number_predictor.components.controls;

import emnist_number_predictor.components.controls.ControlButton.FUNCTION;
import emnist_number_predictor.components.util.Spacer;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class ControlBox extends HBox {

	private static final String RESET_BUTTON_TEXT = "Reset";
	private static final String SAVE_BUTTON_TEXT = "Save Image";

	public ControlBox() {

		this.getChildren().addAll(
			new ControlButton(RESET_BUTTON_TEXT, Pos.CENTER_LEFT, FUNCTION.RESET), 
			new Spacer(),
			new ControlButton(SAVE_BUTTON_TEXT, Pos.CENTER_RIGHT, FUNCTION.SAVE));
	}

}
