package emnist_number_predictor.components.prediction;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PredictionRow extends HBox {

    private DoubleProperty percent = new SimpleDoubleProperty(this, "percent");

    private PredictionLabel digitLabel = new PredictionLabel("", 50);
    private PredictionLabel percentLabel = new PredictionLabel("", 100);
    private ProgressBar progressBar = new ProgressBar(0.0);

    public PredictionRow(int digit) {
        this.setAlignment(Pos.CENTER);

        // ProgressBar
        this.percent.setValue(0.0);
        progressBar.progressProperty().bind(this.percent);
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        
        // Labels
        this.digitLabel.setText(Integer.toString(digit));
        this.percentLabel.setText(String.format("%.2f%%", percent.get()));

        this.getChildren().addAll(digitLabel, percentLabel, progressBar);
    }

    public void setPercent(double normalizedPercent) {   
        // Set the percent to the normalized percentage, a double ranging from [0-1].
        this.percent.setValue(normalizedPercent);
        // Format the normalized percentage for the percentage label.
        double formattedPercent = Math.round((normalizedPercent * 100.0) * 100.0) / 100.0;
		this.percentLabel.setText(formattedPercent + "%");
	}

}
