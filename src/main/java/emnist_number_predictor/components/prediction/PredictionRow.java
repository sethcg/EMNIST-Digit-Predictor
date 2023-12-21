package emnist_number_predictor.components.prediction;

import emnist_number_predictor.components.util.TextLabel;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;

public class PredictionRow {

    public TextLabel digitLabel = new TextLabel("", Pos.CENTER_LEFT, 50);
    public TextLabel percentLabel = new TextLabel(String.format("%.2f%%", 0.0), Pos.CENTER_LEFT, 100);
    public ProgressBar progressBar = new ProgressBar(0.0);

    public PredictionRow(int digit) {
        digitLabel.setText(Integer.toString(digit));
    }

    public void setPercent(double normalizedPercent) { 
        String formattedPercent = String.format("%.2f%%", normalizedPercent * 100);
        percentLabel.setText(formattedPercent);
        progressBar.setProgress(normalizedPercent);
	}

}
