package emnist_number_predictor.app;
import static emnist_number_predictor.util.Const.*;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LoadingScreen extends GridPane{
        
        private VBox loadingScreenContainer = new VBox();

        public ProgressBar loadProgress = new ProgressBar(0.0);
        public DoubleProperty loadPercent = new SimpleDoubleProperty(this, "percent");
        public Label progressText = new Label("Initializing Model...");

        public LoadingScreen() {
            loadProgress.setPrefWidth(INIT_WINDOW_WIDTH - 20);
            loadingScreenContainer.setAlignment(Pos.CENTER);
            loadingScreenContainer.setPadding(new Insets(0,20,0,20));
            loadingScreenContainer.getChildren().addAll(loadProgress, progressText);

            this.setAlignment(Pos.CENTER);
            this.setPrefWidth(INIT_WINDOW_WIDTH);
            this.setPrefWidth(INIT_WINDOW_HEIGHT);

            ProgressBar loadingProgress = new ProgressBar(0.0);
            loadingProgress.setPrefWidth(Double.MAX_VALUE);
            loadingProgress.progressProperty().bind(loadPercent);

            this.addRow(0, loadingScreenContainer);
        }

}