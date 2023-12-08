package emnist_number_predictor.service;

import java.io.IOException;
import emnist_number_predictor.components.InputGrid;
import emnist_number_predictor.components.PredictionGrid;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;

public class AppService {

    public static final int INIT_WIDTH = 400;
    public static final int INIT_HEIGHT = 600;
    
    public ObjectProperty<Double> windowWidth = new SimpleObjectProperty<Double>(this, "windowWidth", null);
    public ObjectProperty<Double> windowHeight = new SimpleObjectProperty<Double>(this, "windowHeight", null);

    public static BorderPane root;
    public static GridService gridService;
    public static PredictionService predictionService;

    public AppService(BorderPane root) throws IOException {
        // Bind the windowWidth and windowHeight to the local variables
        windowWidth.bind(root.widthProperty().asObject());
        windowHeight.bind(root.heightProperty().asObject());

        AppService.gridService = new GridService(this);
        AppService.predictionService = new PredictionService(this);
    }

    public InputGrid setInputGrid() {
        return AppService.gridService.inputGrid = new InputGrid(this);
    }

    public static InputGrid getInputGrid() {
        return AppService.gridService.inputGrid;
    }

    public static GridService getGridService() {
        return AppService.gridService;
    }

    public static PredictionService getPredictionService() {
        return AppService.predictionService;
    }

    public static PredictionGrid getPredictionGrid() {
        return AppService.predictionService.predictionGrid;
    }

}
