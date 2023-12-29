package emnist_number_predictor.util;

import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class HandleMouse<T> implements EventHandler<MouseEvent> {

    Consumer<MouseEvent> method;

    public HandleMouse(Consumer<MouseEvent> method) {
        this.method = method;
    }

    @Override
    public void handle(MouseEvent event) {
        method.accept(event);
        event.consume();
    }
    
}
