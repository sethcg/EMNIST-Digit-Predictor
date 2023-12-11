package emnist_number_predictor.model;
import static emnist_number_predictor.util.Const.*;

import lombok.extern.slf4j.Slf4j;
import emnist_number_predictor.components.window.LoadingService;

@Slf4j
public class ConfigurationProgress {

    public boolean hasModel;

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
                ? PROGRESS.CONFIGURATION.weight + PROGRESS.TRAINING.weight + PROGRESS.EVALUATION.weight
                : PROGRESS.CONFIGURATION.weight;
            double currentProgress = PROGRESS.CONFIGURATION.progress + PROGRESS.TRAINING.progress + PROGRESS.EVALUATION.progress;
            return currentProgress / totalProgress;
        }

    };

    public ConfigurationProgress(boolean hasModel) {
        this.hasModel = hasModel;
    }

    public void incrementProgress(PROGRESS enumValue, String configurationText) {
        log.info(configurationText);
        double percentComplete = enumValue.getProgress(this.hasModel);
        LoadingService.setProgress(percentComplete, configurationText);
    }

}
