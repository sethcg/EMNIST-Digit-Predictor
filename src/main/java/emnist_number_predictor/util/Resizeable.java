package emnist_number_predictor.util;

import emnist_number_predictor.components.window.Window;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;

public class Resizeable {

    // Border padding around the window to listen for resizing mouse event.
    private static final double PULL_EDGE_DEPTH = 10;

    // Absolute max values to allow for resize.
    private static final Rectangle2D SCREEN_BOUNDS = Screen.getPrimary().getVisualBounds();

    public static void addResizeableListener(Window window, Node node) {
    	ResizeableListener resizeListener = new ResizeableListener(window);
		node.addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
		node.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
		node.addEventHandler(MouseEvent.MOUSE_RELEASED, resizeListener);
    }

    private static class ResizeableListener implements EventHandler<MouseEvent> {
    	
        private Window window;

        // Window Bounds
        private final double minWidth, minHeight, maxWidth, maxHeight;

        // Enum that determines the direction in which to adjust the width and height.
        private static enum CHANGE_X { LEFT, RIGHT, NONE; }
        private static enum CHANGE_Y { UP, DOWN, NONE; }

        private CHANGE_X changeX = CHANGE_X.NONE;
        private CHANGE_Y changeY = CHANGE_Y.NONE;

        // Values that represent the starting screen (x, y) for the mouse event.
        double startMouseX, startMouseY;

        // Values that represent the initial values of the top-left and bottom-right corners of the window.
        double startX, startY, startX2, startY2;

    	public ResizeableListener(Window window) {
            this.window = window;

            this.minWidth = window.getMinWidth();
            this.minHeight = window.getMinHeight();
            this.maxWidth = window.getMaxWidth();
            this.maxHeight = window.getMaxHeight();
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
            if(mouseEvent.isSecondaryButtonDown()) return;
            EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();

            final double mouseX = mouseEvent.getSceneX();
            final double mouseY = mouseEvent.getSceneY();
            final double windowWidth = window.getWidth();
            final double windowHeight = window.getHeight();

            final double mouseScreenX = mouseEvent.getScreenX();
            final double mouseScreenY = mouseEvent.getScreenY();


            if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
                if (mouseX < PULL_EDGE_DEPTH && mouseY < PULL_EDGE_DEPTH) {

                    setXYChange(CHANGE_X.LEFT, CHANGE_Y.UP);

                } else if (mouseX < PULL_EDGE_DEPTH && mouseY > windowHeight - PULL_EDGE_DEPTH) {

                    setXYChange(CHANGE_X.LEFT, CHANGE_Y.DOWN);

                } else if (mouseX > windowWidth - PULL_EDGE_DEPTH && mouseY < PULL_EDGE_DEPTH) {

                    setXYChange(CHANGE_X.RIGHT, CHANGE_Y.UP);

                } else if (mouseX > windowWidth - PULL_EDGE_DEPTH && mouseY > windowHeight - PULL_EDGE_DEPTH) {

                    setXYChange(CHANGE_X.RIGHT, CHANGE_Y.DOWN);

                } else if (mouseX < PULL_EDGE_DEPTH) {

                    setXYChange(CHANGE_X.LEFT, CHANGE_Y.NONE);

                } else if (mouseX > windowWidth - PULL_EDGE_DEPTH) {

                    setXYChange(CHANGE_X.RIGHT, CHANGE_Y.NONE);

                } else if (mouseY < PULL_EDGE_DEPTH) {

                    setXYChange(CHANGE_X.NONE, CHANGE_Y.UP);

                } else if (mouseY > windowHeight - PULL_EDGE_DEPTH) {

                    setXYChange(CHANGE_X.NONE, CHANGE_Y.DOWN);

                } else {
                    // No change.
                    setXYChange(CHANGE_X.NONE, CHANGE_Y.NONE);
                }

                startMouseX = mouseScreenX;
                startMouseY = mouseScreenY;

                startX = window.getX(); 
                startY = window.getY();
                startX2 = window.getX() + windowWidth;
                startY2= window.getY() + windowHeight;

            } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) && !(changeX == CHANGE_X.NONE && changeY == CHANGE_Y.NONE)) {
                double dragX = mouseScreenX - startMouseX;
                double dragY = mouseScreenY - startMouseY;
                double min, max;

                // Values that represent the new values of the top-left and bottom-right corners of the window.
                double x = startX; 
                double y = startY;
                double x2 = startX2;
                double y2 = startY2;

                switch (changeX) {
                    case LEFT :
                        min = Math.max(x - maxWidth, 0);
                        max = x2 - minWidth;
                        x = clamp(x + dragX, min, max);
                        break;
                    case RIGHT :
                        min = x + minWidth;
                        max = Math.min(x + maxWidth, SCREEN_BOUNDS.getWidth());
                        x2 = clamp(x2 + dragX, min, max);
                    default :
                        break;
                }
                switch (changeY) {
                    case UP :
                        min = Math.max(y2 - maxHeight, 0);
                        max = y2 - minHeight;
                        y = clamp(y + dragY, min, max);
                        break;
                    case DOWN :
                        min = y + minHeight;
                        max = Math.min(y + maxHeight, SCREEN_BOUNDS.getHeight());
                        y2 = clamp(y2 + dragY, min, max);
                    default :
                        break;
                }

                // Resize window, using the updated values.
                resizeWindow(x, y, x2, y2);

            } else if (MouseEvent.MOUSE_RELEASED.equals(mouseEventType)) {
                setXYChange(CHANGE_X.NONE, CHANGE_Y.NONE);
            }
        }

        private double clamp(double newValue, double minValue, double maxValue) {
            if (newValue < minValue) {
                return minValue;
            }
            if (newValue > maxValue) {
                return maxValue;
            }
            return newValue;
        }

        private void setXYChange(CHANGE_X X, CHANGE_Y Y) {
            changeX = X;
            changeY = Y;
        }

        private void resizeWindow(double x1, double y1, double x2, double y2) {
            window.setX(x1);
            window.setY(y1);
            window.setWidth(x2 - x1);
            window.setHeight(y2 - y1);
        }
    }


}