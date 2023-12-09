package emnist_number_predictor.model;
import static emnist_number_predictor.util.Const.*;

import java.io.File;
import emnist_number_predictor.app.App;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ConfigurationProgress {

    private boolean hasModel = new File(MODEL_PATH).exists();
    private DoubleProperty progress = new SimpleDoubleProperty(this, "percent");

    private static final double CONFIGURATION_WEIGHT_PERCENTAGE = 0.2;
    private static final double TRAINING_WEIGHT_PERCENTAGE = 0.7;
    private static final double EVALUATION_WEIGHT_PERCENTAGE = 0.1;

    private static final double CONFIGURATION_TOTAL = 4;
    private static final double EVALUATION_TOTAL = 2;

    public enum PROGRESS {
        CONFIGURATION(CONFIGURATION_WEIGHT_PERCENTAGE, CONFIGURATION_TOTAL), 
        TRAINING(TRAINING_WEIGHT_PERCENTAGE, (double) EPOCH_NUM), 
        EVALUATION(EVALUATION_WEIGHT_PERCENTAGE, EVALUATION_TOTAL);
        
        private final double weight;
        private final double total;
        private double count;
        private double progress;

        private PROGRESS(double weight, double total) {
            this.weight = weight;
            this.total = total;
            this.count = 0;
        }

        public double getProgress(boolean hasModel) {
            this.count += 1.0;
            this.progress = weight * (count / total);

            double totalProgress = hasModel
                ? PROGRESS.CONFIGURATION.weight
                : PROGRESS.CONFIGURATION.weight + PROGRESS.TRAINING.weight + PROGRESS.EVALUATION.weight;
            double currentProgress = PROGRESS.CONFIGURATION.progress + PROGRESS.TRAINING.progress + PROGRESS.EVALUATION.progress;
            return currentProgress / totalProgress;
        }

    };

    public ConfigurationProgress() {
        App.loadingScreen.loadProgress.progressProperty().bind(progress);
    }

    public void incrementProgress(PROGRESS enumValue) {
        this.progress.set(enumValue.getProgress(this.hasModel));
    }

}
