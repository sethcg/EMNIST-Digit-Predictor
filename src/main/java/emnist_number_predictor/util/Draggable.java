package emnist_number_predictor.util;

import emnist_number_predictor.components.window.Window;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class Draggable {

    public static void addDraggableListener(Window window, Node node) {
    	DraggableListener resizeListener = new DraggableListener(window);
		node.addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
		node.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
    }

    private static class DraggableListener implements EventHandler<MouseEvent> {
    	private Window window;
    	private double x, y;
    	
    	public DraggableListener(Window window) {
            this.window = window;
        }
    	
    	@Override
    	public void handle(MouseEvent mouseEvent) {
            EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
            if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType) == true) {
            	x = mouseEvent.getSceneX();
                y = mouseEvent.getSceneY();
    		} else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) == true) {
    			window.setX(mouseEvent.getScreenX() - x);
            	window.setY(mouseEvent.getScreenY() - y);
    		}
    	}
    }

}