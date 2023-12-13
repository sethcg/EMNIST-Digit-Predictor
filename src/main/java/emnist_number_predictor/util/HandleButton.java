package emnist_number_predictor.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class HandleButton<T> implements EventHandler<ActionEvent> {

    Runnable method;

    public HandleButton(Runnable method) {
        this.method = method;
    }

    @Override
    public void handle(ActionEvent event) {
        method.run();
    }
    
}
