package emnist_number_predictor.components.prediction;

import emnist_number_predictor.components.util.TextLabel;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public class PredictionRow extends HBox {

    private TextLabel digitLabel = new TextLabel("", 50);
    private TextLabel percentLabel = new TextLabel("", 100);
    private ProgressBar progressBar = new ProgressBar(0.0);

    public PredictionRow(int digit) {
        this.setAlignment(Pos.CENTER);
        this.initialize(digit);
    }

    private void initialize(int digit) {
        double percent = 0.0;
        String formattedPercent = String.format("%.2f%%", 0.0);

        this.digitLabel.setText(Integer.toString(digit));
        this.percentLabel.setText(formattedPercent);
        this.progressBar.setProgress(percent);

        this.getChildren().addAll(digitLabel, percentLabel, progressBar);
    }

    public void setPercent(double normalizedPercent) { 
        String formattedPercent = String.format("%.2f%%", normalizedPercent * 100);
        this.percentLabel.setText(formattedPercent);
        this.progressBar.setProgress(normalizedPercent);
	}

}
