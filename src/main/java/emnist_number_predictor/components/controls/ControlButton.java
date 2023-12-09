package emnist_number_predictor.components.controls;

import javafx.geometry.Pos;
import emnist_number_predictor.app.App;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ControlButton extends Button {

    private static final String CONTROL_BUTTON_DEFAULT_STYLE = "control-button-default";

    public ControlButton(String text, Pos position, FUNCTION enumValue) {
        this.setPrefWidth(USE_COMPUTED_SIZE);
        this.setText(text);
        this.getStyleClass().add(CONTROL_BUTTON_DEFAULT_STYLE);
		this.setOnAction(enumValue.eventHandler());
        this.setAlignment(position);
    }

    // Enum to store each control button EventHandler
    public enum FUNCTION {
        SAVE   { 
            EventHandler<ActionEvent> eventHandler() { 
                return new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) { App.controller.saveScreenshot(); }
                };
            }
        },
        RESET  { 
            EventHandler<ActionEvent> eventHandler() { 
                return new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) { App.controller.resetPrediction(); }
                };
            }
        };
        abstract EventHandler<ActionEvent> eventHandler();
    }

}
