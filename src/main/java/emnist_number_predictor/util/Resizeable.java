package emnist_number_predictor.util;

import emnist_number_predictor.components.window.Window;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class Resizeable {

    private static final double BOTTOM_CORNER_OFFSET = 15;

    public static void addResizeListener(Window window, Node node) {
        ResizeListener resizeListener = new ResizeListener(window);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
    }

    static class ResizeListener implements EventHandler<MouseEvent> {
        private Window window;
        private Boolean resize = false;
        private double dx, dy;

        public ResizeListener(Window window) {
            this.window = window;
        }

        @Override
        public void handle(MouseEvent event) {
            EventType<? extends MouseEvent> mouseEventType = event.getEventType();
            if(mouseEventType.equals(MouseEvent.MOUSE_PRESSED)) {
                if (event.getX() > window.getWidth() - BOTTOM_CORNER_OFFSET && 
                    event.getX() < window.getWidth() + BOTTOM_CORNER_OFFSET && 
                    event.getY() > window.getHeight() - BOTTOM_CORNER_OFFSET && 
                    event.getY() < window.getHeight() + BOTTOM_CORNER_OFFSET) 
                {
                    resize = true;
                    dx = window.getWidth() - event.getX();
                    dy = window.getHeight() - event.getY();
                } else {
                    resize = false;
                }
            }

            if(mouseEventType.equals(MouseEvent.MOUSE_DRAGGED)) {
                if (resize) {
                    window.setWidth(event.getX() + dx);
                    window.setHeight(event.getY() + dy);
                }
            }
        }

    }
}